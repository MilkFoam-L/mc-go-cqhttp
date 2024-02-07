package com.milkfoam.mcgocqhttp.module

import com.milkfoam.mcgocqhttp.McGoCqHttp.configData
import com.milkfoam.mcgocqhttp.api.event.UserQuitGroupEvent
import com.milkfoam.mcgocqhttp.data.PostData
import com.milkfoam.mcgocqhttp.database.DataBaseManager
import com.milkfoam.mcgocqhttp.utils.consoleInfoMsg
import com.milkfoam.mcgocqhttp.utils.debugInfo
import com.milkfoam.mcgocqhttp.utils.withNotNull
import org.bukkit.Bukkit
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.console
import taboolib.common5.util.parseUUID

object CheckUser {

    //onMessageHandler: {
    // "post_type":"notice",
    // "notice_type":"group_decrease",
    // "time":1703925498,
    // "self_id":2707046905,
    // "sub_type":"kick",
    // "group_id":852244501,
    // "operator_id":2479640157,
    // "user_id":705302332
    // }

    @SubscribeEvent
    fun starCheck(e: UserQuitGroupEvent) {

        // 获取响应消息
        val postData: PostData = e.postData
        // 获取配置文件
        val data = configData?.checkUser ?: return debugInfo("检测用户配置文件为空")
        // 判断是否开启
        if (!data.enable) return debugInfo("检测用户功能未开启")
        // 判断是否在群组中
        if (!data.groups.contains(postData.groupId)) return debugInfo("该群组未开启检测功能")
        // 检查是否为空
        if (postData.userId == null) return debugInfo("检测用户功能检测的账号为空")

        DataBaseManager.getPlayerUUID(postData.userId).parseUUID().withNotNull {
            val player = Bukkit.getOfflinePlayer(it)
            val name = player.name
            DataBaseManager.removeFromQQ(postData.userId)
            consoleInfoMsg("&c检测到账号 ${postData.userId} 退出群聊，已将 $name 从数据库中移除")
            if (player.isOnline) {
                player.player?.kickPlayer("检测到您的账号已退出群聊，已将您踢出服务器")
            }
        }


    }

}