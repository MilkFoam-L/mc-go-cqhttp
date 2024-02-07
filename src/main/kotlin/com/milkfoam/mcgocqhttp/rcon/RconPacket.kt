package com.milkfoam.mcgocqhttp.rcon

import com.milkfoam.mcgocqhttp.rcon.ex.MalformedPacketException
import java.io.*
import java.net.SocketException
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class RconPacket private constructor(val requestId: Int, val type: Int, val payload: ByteArray) {
    companion object {
        const val SERVERDATA_EXECCOMMAND: Int = 2
        const val SERVERDATA_AUTH: Int = 3

        /**
         * Send a Rcon packet and fetch the response
         *
         * @param rcon Rcon instance
         * @param type The packet type
         * @param payload The payload (password, command, etc.)
         * @return A RconPacket object containing the response
         *
         * @throws IOException
         * @throws MalformedPacketException
         */
        @Throws(IOException::class)
        fun send(rcon: Rcon, type: Int, payload: ByteArray): RconPacket {
            try {
                write(rcon.socket!!.getOutputStream(), rcon.requestId, type, payload)
            } catch (se: SocketException) {
                // Close the socket if something happens
                rcon.socket!!.close()


                // Rethrow the exception
                throw se
            }

            return read(rcon.socket!!.getInputStream())
        }

        /**
         * Write a rcon packet on an outputstream
         *
         * @param out The OutputStream to write on
         * @param requestId The request id
         * @param type The packet type
         * @param payload The payload
         *
         * @throws IOException
         */
        @Throws(IOException::class)
        private fun write(out: OutputStream, requestId: Int, type: Int, payload: ByteArray) {
            val bodyLength = getBodyLength(payload.size)
            val packetLength = getPacketLength(bodyLength)

            val buffer = ByteBuffer.allocate(packetLength)
            buffer.order(ByteOrder.LITTLE_ENDIAN)

            buffer.putInt(bodyLength)
            buffer.putInt(requestId)
            buffer.putInt(type)
            buffer.put(payload)


            // Null bytes terminators
            buffer.put(0.toByte())
            buffer.put(0.toByte())


            // Woosh!
            out.write(buffer.array())
            out.flush()
        }

        /**
         * Read an incoming rcon packet
         *
         * @param in The InputStream to read on
         * @return The read RconPacket
         *
         * @throws IOException
         * @throws MalformedPacketException
         */
        @Throws(IOException::class)
        private fun read(`in`: InputStream): RconPacket {
            // Header is 3 4-bytes ints
            val header = ByteArray(4 * 3)


            // Read the 3 ints
            `in`.read(header)

            try {
                // Use a bytebuffer in little endian to read the first 3 ints
                val buffer = ByteBuffer.wrap(header)
                buffer.order(ByteOrder.LITTLE_ENDIAN)

                val length = buffer.getInt()
                val requestId = buffer.getInt()
                val type = buffer.getInt()


                // Payload size can be computed now that we have its length
                val payload = ByteArray(length - 4 - 4 - 2)

                val dis = DataInputStream(`in`)


                // Read the full payload
                dis.readFully(payload)


                // Read the null bytes
                dis.read(ByteArray(2))

                return RconPacket(requestId, type, payload)
            } catch (e: BufferUnderflowException) {
                throw MalformedPacketException("Cannot read the whole packet")
            } catch (e: EOFException) {
                throw MalformedPacketException("Cannot read the whole packet")
            }
        }

        private fun getPacketLength(bodyLength: Int): Int {
            // 4 bytes for length + x bytes for body length
            return 4 + bodyLength
        }

        private fun getBodyLength(payloadLength: Int): Int {
            // 4 bytes for requestId, 4 bytes for type, x bytes for payload, 2 bytes for two null bytes
            return 4 + 4 + payloadLength + 2
        }
    }
}