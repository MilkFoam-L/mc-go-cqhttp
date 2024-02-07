package com.milkfoam.mcgocqhttp.api

import com.milkfoam.mcgocqhttp.data.GroupUserData
import com.milkfoam.mcgocqhttp.data.PostData
import com.milkfoam.mcgocqhttp.data.SenderData
import com.milkfoam.mcgocqhttp.database.DataBaseManager
import com.milkfoam.mcgocqhttp.service.request.*
import java.util.*

object MGCAPI {

    /**
     * 禁言某个群的QQ
     * @param groupId 群号
     * @param userId QQ
     * @param duration 单位为秒 0的时候取消禁言
     */
    fun groupBan(groupId: String, userId: String, duration: Int) {
        GroupController().groupBan(groupId, userId, duration)
    }

    /**
     * 控制某个群的全员禁言
     * @param groupId 群号
     * @param enable 是否开启 (true|false)
     */
    fun groupBan(groupId: String, enable: String) {
        GroupController().groupAllBan(groupId, enable)
    }

    /**
     * 踢出某个群的某个QQ
     * @param groupId String
     * @param userId String
     * @param rejectAddRequest 是否拒绝此人的加群请求 (true|false)
     */
    fun groupKick(groupId: String, userId: String, rejectAddRequest: String) {
        GroupController().groupKick(groupId, userId, rejectAddRequest)
    }

    /**
     * 修改某个群某个QQ的群名片
     * @param groupId String
     * @param userId String
     * @param card String 群名片内容, 不填或空字符串表示删除群名片
     */
    fun setGroupCard(groupId: String, userId: String, card: String) {
        GroupController().setGroupCard(groupId, userId, card)
    }

    /**
     * 获取玩家绑定的QQ
     * @param uuid UUID
     * @return String
     */
    fun getQQ(uuid: UUID): String {
        return DataBaseManager.getQQ(uuid)
    }

    /**
     * 获取qq绑定的玩家
     * @param qq String
     * @return String
     */
    fun getPlayer(qq: String): String {
        return DataBaseManager.getPlayerUUID(qq)
    }

    /**
     * 判断qq是否已经绑定
     * @param qq String
     * @return Boolean
     */
    fun checkQQ(qq: String): Boolean {
        return DataBaseManager.checkQQ(qq)
    }

    /**
     * 保存玩家信息
     * @param uuid UUID
     * @param qq String
     */
    fun saveQQ(uuid: UUID, qq: String) {
        DataBaseManager.saveQQ(uuid, qq)
    }

    /**
     * 通过uuid删除玩家数据
     * @param uuid UUID
     */
    fun remove(uuid: UUID) {
        DataBaseManager.removeFromUUID(uuid)
    }

    /**
     * 通过qq删除玩家数据
     * @param qq String
     */
    fun remove(qq: String) {
        DataBaseManager.removeFromQQ(qq)
    }

    /**
     * 获取数据库中玩家的绑定信息 uuid:QQ
     * @return MutableMap<String, String>
     */
    fun getPlayerData(): MutableMap<String, String> {
        return DataBaseManager.getPlayerData()
    }

    /**
     * 获取数据库中的所有qq
     * @return MutableList<String>
     */
    fun getQQList(): MutableList<String> {
        return DataBaseManager.getQQList()
    }

    /**
     * 获取指定群聊的所有QQ的详细信息
     * @param groupId String
     * @return List<GroupMember>
     */
    fun getGroupMemberList(groupId: String): List<GroupUserData> {
        return GetGroupInfo().getGroupUserDataList(groupId)
    }

    /**
     * 获取指定账号的QQ信息
     * @param qq String
     * @return String
     */
    fun getUserInfo(qq: String): SenderData? {
        return GetUserInfo().getData(qq)
    }

    /**
     * 发送私聊消息
     * @param userId String
     * @param message String
     */
    fun sendPrivateMessage(userId: String, message: String) {
        PrivateMessage().send(userId, message)
    }

    /**
     * 发送群组消息
     * @param groupId String
     * @param message String
     */
    fun sendGroupMessage(groupId: String, message: String) {
        GroupMessage().send(groupId, message)
    }

    /**
     * 艾特玩家
     * @param qq String
     * @return String
     */
    fun atQQUser(qq: String): String {
        return "[CQ:at,qq=${qq}]"
    }

    /**
     * 发送图片
     * @param url String
     * @return String
     */
    fun getUrlImage(url: String): String {
        // [CQ:image,file=http://baidu.com/1.jpg,type=show,id=40004]
        return "[CQ:image,file=${url},cache=0]"
    }

    /**
     * 同意进群
     * @param postData PostData
     */
    fun acceptJoinGroup(postData: PostData) {
        GroupController().acceptJoinGroup(postData)
    }

    /**
     * 拒绝进群
     * @param postData PostData
     * @param reason String
     */
    fun rejectJoinGroup(postData: PostData, reason: String) {
        GroupController().rejectJoinGroup(postData, reason)
    }

}