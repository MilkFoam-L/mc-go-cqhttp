package com.milkfoam.mcgocqhttp.database

import com.milkfoam.mcgocqhttp.io.ConfigurationReader.storage
import taboolib.module.database.*
import javax.sql.DataSource

class DataBaseSQL : DataBase() {

    override val host: Host<SQL> = storage.getHost("SQL")

    override val userTable = Table("cqhttpData", host) {
        add("name") {
            type(ColumnTypeSQL.VARCHAR, 36)
        }
        add("uuid") {
            type(ColumnTypeSQL.VARCHAR, 36)
        }
        add("qq") {
            type(ColumnTypeSQL.VARCHAR, 36)
        }
    }

    override val source: DataSource by lazy {
        host.createDataSource()
    }

    init {
        userTable.createTable(source)
    }

}