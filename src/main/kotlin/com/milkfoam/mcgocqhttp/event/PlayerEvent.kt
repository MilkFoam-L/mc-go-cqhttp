package com.milkfoam.mcgocqhttp.event

import com.milkfoam.mcgocqhttp.module.AccountBind
import com.milkfoam.mcgocqhttp.module.AccountBind.cacheInBind
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.SubscribeEvent

object PlayerEvent {

    /**
     * 玩家退出游戏删除缓存
     * @param e PlayerQuitEvent
     */
    @SubscribeEvent
    fun quit(e: PlayerQuitEvent) {
        val player = e.player
        val uuid = player.uniqueId
        if (cacheInBind.containsKey(uuid)) {
            cacheInBind.remove(uuid)
        }
    }

    /**
     * 玩家被踢出游戏删除缓存
     * @param e PlayerKickEvent
     */
    @SubscribeEvent
    fun kick(e: PlayerKickEvent) {
        val player = e.player
        val uuid = player.uniqueId
        if (cacheInBind.containsKey(uuid)) {
            cacheInBind.remove(uuid)
        }
    }

}