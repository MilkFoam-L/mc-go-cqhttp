package com.milkfoam.mcgocqhttp.database

import com.milkfoam.mcgocqhttp.utils.isNotNull
import org.bukkit.Bukkit
import taboolib.module.database.Host
import taboolib.module.database.Table
import java.util.*
import javax.sql.DataSource

abstract class DataBase {

    abstract val host: Host<*>

    abstract val userTable: Table<*, *>

    abstract val source: DataSource

    fun getQQ(uniqueId: UUID): String {
        val table = userTable
        return table.select(source) {
            rows("qq")
            where { "uuid" eq uniqueId.toString() }
            limit(1)
        }.firstOrNull {
            getString("qq")
        } ?: let {
            "未绑定"
        }
    }

    fun saveQQ(uniqueId: UUID, data: String) {
        val query = userTable.find(source) {
            where {
                "uuid" eq uniqueId.toString()
            }
        }
        val name = Bukkit.getOfflinePlayer(uniqueId).name.isNotNull()
        if (query) {
            userTable.update(source) {
                where { "uuid" eq uniqueId.toString() }
                set("qq", data)
                set("name", name)
            }
        } else {
            userTable.insert(source, "name", "uuid", "qq") {
                value(name, uniqueId.toString(), data)
            }
        }
    }

    fun findQQ(qq: String): Boolean {
        val table = userTable
        return table.find(source) {
            rows("qq")
            where { "qq" eq qq }
        }
    }

    fun getPlayerUUID(qq: String): String {
        val table = userTable
        return table.select(source) {
            rows("uuid")
            where { "qq" eq qq }
            limit(1)
        }.firstOrNull {
            this.getString("uuid")
        } ?: let {
            "未找到"
        }
    }

    fun getAllData(): MutableMap<String, String> {
        val table = userTable
        val dataMap = mutableMapOf<String, String>()
        table.select(source) {
            rows("uuid", "qq")
        }.forEach {
            val uuid = getString("uuid")
            val qq = getString("qq")
            dataMap[uuid] = qq
        }
        return dataMap
    }

    fun getAllQQ(): MutableList<String> {
        val table = userTable
        val dataList = mutableListOf<String>()
        table.select(source) {
            rows("qq")
        }.forEach {
            dataList.add(getString("qq"))
        }
        return dataList
    }

    fun remove(uniqueId: UUID) {
        userTable.delete(source) {
            where { "uuid" eq uniqueId.toString() }
        }
    }

    fun remove(qq: String) {
        userTable.delete(source) {
            where { "qq" eq qq }
        }
    }

}