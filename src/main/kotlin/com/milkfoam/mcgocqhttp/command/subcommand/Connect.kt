package com.milkfoam.mcgocqhttp.command.subcommand

import com.milkfoam.mcgocqhttp.McGoCqHttp.runBot
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.subCommand
import taboolib.platform.util.sendLang

object Connect {

    val connect = subCommand {
        execute<CommandSender> { sender, _, _ ->
            runBot()
            sender.sendLang("reload-bot")
        }
    }

}