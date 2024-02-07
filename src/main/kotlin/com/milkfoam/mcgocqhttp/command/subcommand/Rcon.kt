package com.milkfoam.mcgocqhttp.command.subcommand

import com.milkfoam.mcgocqhttp.McGoCqHttp.connectRcon
import com.milkfoam.mcgocqhttp.McGoCqHttp.runBot
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.subCommand
import taboolib.platform.util.sendLang

object Rcon {

    val rconReload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            connectRcon()
            sender.sendLang("reload-rcon")
        }
    }

}