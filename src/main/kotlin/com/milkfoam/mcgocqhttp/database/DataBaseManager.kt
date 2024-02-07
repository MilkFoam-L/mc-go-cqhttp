package com.milkfoam.mcgocqhttp.database

import com.milkfoam.mcgocqhttp.McGoCqHttp.configData
import java.util.*

object DataBaseManager {

    /**
     * 注册数据库实例
     */
    private val dataBase: DataBase = if (configData?.storageMode == true) {
        DataBaseSQL()
    } else {
        DataBaseSQLite()
    }

    /**
     * 获取玩家的QQ号
     * @param uniqueId UUID
     * @return String
     */
    fun getQQ(uniqueId: UUID): String {
        return dataBase.getQQ(uniqueId)
    }

    /**
     * 检查玩家是否绑定QQ
     * @param qq String
     * @return Boolean
     */
    fun checkQQ(qq: String): Boolean {
        return dataBase.findQQ(qq)
    }

    /**
     * 通过QQ获取玩家数据库的UUID
     * @param qq String
     * @return String
     */
    fun getPlayerUUID(qq: String): String {
        return dataBase.getPlayerUUID(qq)
    }

    /**
     * 获取所有绑定的QQ号
     * @return MutableList<String>
     */
    fun getQQList(): MutableList<String> {
        return dataBase.getAllQQ()
    }

    /**
     * 获取所有玩家的数据
     * @return Pair<String, String>
     */
    fun getPlayerData(): MutableMap<String, String> {
        return dataBase.getAllData()
    }

    /**
     * 保存玩家的数据
     * @param uniqueId UUID
     * @param data String
     */
    fun saveQQ(uniqueId: UUID, data: String) {
        dataBase.saveQQ(uniqueId, data)
    }

    /**
     * 通过UUID删除玩家数据
     * @param uniqueId UUID
     */
    fun removeFromUUID(uniqueId: UUID) {
        dataBase.remove(uniqueId)
    }

    /**
     * 通过QQ删除玩家数据
     * @param qq String
     */
    fun removeFromQQ(qq: String) {
        dataBase.remove(qq)
    }

}