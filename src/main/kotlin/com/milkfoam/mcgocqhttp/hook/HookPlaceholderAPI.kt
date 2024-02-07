package com.milkfoam.mcgocqhttp.hook

import com.milkfoam.mcgocqhttp.database.DataBaseManager
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import taboolib.platform.compat.PlaceholderExpansion

object HookPlaceholderAPI : PlaceholderExpansion {

    override val identifier = "mgc"

    //       0    1
    // %mgc_qq_玩家名字%
    // 获取玩家QQ
    override fun onPlaceholderRequest(player: OfflinePlayer?, args: String): String {

        val arg = args.split("_")

        when (arg[0]) {
            "qq" -> {
                val offlinePlayer = Bukkit.getOfflinePlayer(arg[1])
                return DataBaseManager.getQQ(offlinePlayer.uniqueId)
            }

            else -> {
                return "获取错误"
            }
        }

    }

}