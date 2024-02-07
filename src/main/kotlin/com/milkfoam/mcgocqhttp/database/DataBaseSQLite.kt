package com.milkfoam.mcgocqhttp.database

import taboolib.common.platform.function.getDataFolder
import taboolib.module.database.*
import java.io.File
import javax.sql.DataSource

class DataBaseSQLite : DataBase() {

    override val host: Host<SQLite> = File(getDataFolder(), "data.db").getHost()

    override val userTable = Table("cqhttpData", host) {
        add("name") {
            type(ColumnTypeSQLite.TEXT, 36)
        }
        add("uuid") {
            type(ColumnTypeSQLite.TEXT, 36)
        }
        add("qq") {
            type(ColumnTypeSQLite.TEXT, 36)
        }
    }

    override val source: DataSource by lazy {
        host.createDataSource()
    }

    init {
        userTable.createTable(source)
    }
}