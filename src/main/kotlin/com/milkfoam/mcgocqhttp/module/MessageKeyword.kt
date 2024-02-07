package com.milkfoam.mcgocqhttp.module

import com.milkfoam.mcgocqhttp.McGoCqHttp.configData
import com.milkfoam.mcgocqhttp.McGoCqHttp.rcon
import com.milkfoam.mcgocqhttp.api.MGCAPI
import com.milkfoam.mcgocqhttp.api.event.QQMessageEvent
import com.milkfoam.mcgocqhttp.rcon.Rcon
import com.milkfoam.mcgocqhttp.utils.debugInfo
import com.milkfoam.mcgocqhttp.utils.replacePAPI
import com.milkfoam.mcgocqhttp.utils.withNotNull
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common5.util.parseUUID
import taboolib.common5.util.replace
import taboolib.module.chat.uncolored
import taboolib.platform.compat.replacePlaceholder
import java.io.File.separator

object MessageKeyword {

    @SubscribeEvent
    fun messageKeyword(e: QQMessageEvent) {
        // 获取配置文件
        val data = configData?.messageKeyword ?: return debugInfo("消息关键词: 配置文件为空")
        val postData = e.data
        // 是否开启
        if (!data.enable) return debugInfo("消息关键词: 关闭")
        // 获取消息
        val message = postData.rawMessage
        // 获取用户
        val userId = postData.userId
        // 获取群
        val groupId = postData.groupId
        // 消息为空
        if (message == null || userId == null || groupId == null) return debugInfo("消息关键词: 消息为空")

        data.keys.forEach {
            // 判断消息是否包含关键词
            if (message != it.key) return@forEach debugInfo("消息关键词: 消息不完全匹配关键词")

            when (it.isConsoleCmd) {
                true -> {
                    if (it.command == null) return@forEach debugInfo("消息关键词: 命令为空")
                    // 获取返回值
                    val result = rcon?.command(it.command) ?: return debugInfo("消息关键词: Rcon为空")
                    val request =
                        it.request?.replace(
                            Pair("{value}", result),
                            Pair("{at}", MGCAPI.atQQUser(userId))
                        )?.uncolored()
                    debugInfo("request:${request}")
                    request?.joinToString(separator = "\n")
                        .withNotNull { str ->
                            val uuid = MGCAPI.getPlayer(userId).parseUUID()
                            if (uuid == null) {
                                MGCAPI.sendGroupMessage(groupId, str)
                                if (str.count { count -> count == '%' } > 2) {
                                    MGCAPI.sendGroupMessage(groupId, "此账号未进行绑定!所以PAPI变量不会生效!")
                                }
                            } else {
                                val player = Bukkit.getOfflinePlayer(uuid)
                                MGCAPI.sendGroupMessage(groupId, str.replacePAPI(player))
                            }
                        }
                }

                false -> {
                    it.customMessage?.joinToString(separator = "\n")
                        ?.replace(Pair("{at}", MGCAPI.atQQUser(userId))).withNotNull { str ->
                            val uuid = MGCAPI.getPlayer(userId).parseUUID()
                            if (uuid == null) {
                                MGCAPI.sendGroupMessage(groupId, str)
                                if (str.count { count -> count == '%' } > 2) {
                                    MGCAPI.sendGroupMessage(groupId, "此账号未进行绑定!所以PAPI变量不会生效!")
                                }
                            } else {
                                val player = Bukkit.getOfflinePlayer(uuid)
                                MGCAPI.sendGroupMessage(groupId, str.replacePAPI(player))
                            }
                        }
                }

                else -> return
            }

        }


    }

}