package com.milkfoam.mcgocqhttp.module


import com.milkfoam.mcgocqhttp.McGoCqHttp
import com.milkfoam.mcgocqhttp.api.MGCAPI.getQQ
import com.milkfoam.mcgocqhttp.utils.debugInfo
import com.milkfoam.mcgocqhttp.utils.isNotNull
import org.bukkit.entity.Player
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import taboolib.module.chat.colored
import taboolib.platform.compat.replacePlaceholder
import java.util.*

object WhiteList {


    @SubscribeEvent
    fun join(e: PlayerJoinEvent) {
        val data = McGoCqHttp.configData?.whiteList
        if (data == null || !data.enable) return debugInfo("白名单功能： 进入阻止未启用")

        val player = e.player
        if (data.cancelJoinEnable && checkQQ(player.uniqueId)) {
            debugInfo("玩家 ${player.name} 未进行白名单绑定!开始任务")
            var time = 0
            val kickTime = data.cancelJoinKickTime
            submit(period = 20) {
                time++
                if (time >= kickTime && checkQQ(player.uniqueId)) {
                    player.kickPlayer(sendWhiteListMessage(player))
                    debugInfo("玩家 ${player.name} 到达时间未进行绑定！踢出服务器！")
                    cancel()
                }
                if (!checkQQ(player.uniqueId)) {
                    debugInfo("玩家 ${player.name} 绑定成功！取消任务")
                    cancel()
                }
                if (!player.isOnline){
                    debugInfo("玩家 ${player.name} 不在线！取消任务")
                    cancel()
                }
            }
        }
    }

    @SubscribeEvent
    fun move(e: PlayerMoveEvent) {
        val data = McGoCqHttp.configData?.whiteList
        if (data == null || !data.enable) return
        val player = e.player
        if (data.cancelMove && checkQQ(player.uniqueId)) {
            e.isCancelled = true
            player.sendMessage(sendWhiteListMessage(player))
        }
    }

    @SubscribeEvent
    fun chat(e: AsyncPlayerChatEvent) {
        val data = McGoCqHttp.configData?.whiteList
        if (data == null || !data.enable) return

        val player = e.player
        if (data.cancelChat && checkQQ(player.uniqueId)) {
            e.isCancelled = true
            player.sendMessage(sendWhiteListMessage(player))
        }
    }

    @SubscribeEvent
    fun cmd(e: PlayerCommandPreprocessEvent) {
        val data = McGoCqHttp.configData?.whiteList
        if (data == null || !data.enable) return
        val player = e.player
        if (data.cancelCommand && checkQQ(player.uniqueId)) {
            e.isCancelled = true
            player.sendMessage(sendWhiteListMessage(player))
        }
    }

    private fun checkQQ(uuid: UUID): Boolean {
        return getQQ(uuid) == "未绑定"
    }

    private fun sendWhiteListMessage(player: Player): String {
        val data = McGoCqHttp.configData?.whiteList
        return data?.message?.replace("{player}", player.name)?.replacePlaceholder(player)?.colored().isNotNull()
    }


}