package com.milkfoam.mcgocqhttp.rcon

import com.milkfoam.mcgocqhttp.rcon.ex.AuthenticationException
import com.milkfoam.mcgocqhttp.utils.withNotNull
import java.io.IOException
import java.net.Socket
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*

// 遵循开源协议 https://github.com/kr5ch/rkon-core
class Rcon(host: String?, port: Int, password: ByteArray?) {
    private val sync = Any()
    private val rand = Random()

    var requestId: Int = 0
        private set
    var socket: Socket? = null
        private set

    // Default charset is utf8
    var charset: Charset = StandardCharsets.UTF_8

    /**
     * Create, connect and authenticate a new Rcon object
     *
     * @param host Rcon server address
     * @param port Rcon server port
     * @param password Rcon server password
     */
    init {
        // Connect to host
        this.connect(host, port, password)
    }

    /**
     * Connect to a rcon server
     *
     * @param host Rcon server address
     * @param port Rcon server port
     * @param password Rcon server password
     */
    @Throws(IOException::class, AuthenticationException::class)
    fun connect(host: String?, port: Int, password: ByteArray?) {
        require(!(host == null || host.trim { it <= ' ' }.isEmpty())) { "Host can't be null or empty" }

        require(!(port < 1 || port > 65535)) { "Port is out of range" }


        // Connect to the rcon server
        synchronized(sync) {
            // New random request id
            this.requestId = rand.nextInt()


            // We can't reuse a socket, so we need a new one
            this.socket = Socket(host, port)
        }


        // Send the auth packet
        val res = password.withNotNull {
            this.send(RconPacket.SERVERDATA_AUTH, it)
        }


        // Auth failed
        if (res?.requestId == -1) {
            throw AuthenticationException("Password rejected by server")
        }
    }

    /**
     * Disconnect from the current server
     *
     */
    @Throws(IOException::class)
    fun disconnect() {
        synchronized(sync) {
            socket!!.close()
        }
    }

    /**
     * Send a command to the server
     *
     * @param payload The command to send
     * @return The payload of the response
     */
    @Throws(IOException::class)
    fun command(payload: String?): String {
        require(!(payload == null || payload.trim { it <= ' ' }.isEmpty())) { "Payload can't be null or empty" }

        val response = this.send(RconPacket.SERVERDATA_EXECCOMMAND, payload.toByteArray())

        return String(response.payload, this.charset)
    }

    @Throws(IOException::class)
    private fun send(type: Int, payload: ByteArray): RconPacket {
        synchronized(sync) {
            return RconPacket.send(this, type, payload)
        }
    }
}