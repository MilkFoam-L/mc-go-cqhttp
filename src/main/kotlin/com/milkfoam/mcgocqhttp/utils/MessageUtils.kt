package com.milkfoam.mcgocqhttp.utils

import com.milkfoam.mcgocqhttp.McGoCqHttp.configData
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.console
import taboolib.common.platform.function.onlinePlayers
import taboolib.module.chat.colored
import java.awt.SystemColor.text

private const val PREFIX = "mc-go-cqhttp"

fun prefixInfo(text: Any?) {
    console().sendMessage("$text".colored())
}

fun debugInfo(text: Any?) {
    if (configData?.debug == true) {
        console().sendMessage("&7[&a$PREFIX&7] &6[&cDEBUG&6] $text".colored())
    }
}

fun Player.sendInfoMsg(text: String) {
    this.sendMessage("&7[&a$PREFIX&7] $text".colored())
}

fun Player.sendErrorMsg(text: String) {
    this.sendMessage("&7[&c$PREFIX&7] $text".colored())
}

fun consoleInfoMsg(text: String) {
    console().sendMessage("&7[&a$PREFIX&7] $text".colored())
}

fun consoleErrorMsg(text: String) {
    console().sendMessage("&7[&c$PREFIX&7] $text".colored())
}

fun CommandSender.sendInfoMsg(text: String) {
    this.sendMessage("&7[&a$PREFIX&7] $text".colored())
}

fun CommandSender.sendErrorMsg(text: String) {
    this.sendMessage("&7[&c$PREFIX&7] $text".colored())
}

fun ProxyPlayer.sendInfoMsg(text: String) {
    this.sendMessage("&7[&a$PREFIX&7] $text".colored())
}

fun sendOnlinePlayer(text: String) {
    onlinePlayers().forEach {
        it.sendMessage("&7[&c$PREFIX&7] $text".colored())
    }
}