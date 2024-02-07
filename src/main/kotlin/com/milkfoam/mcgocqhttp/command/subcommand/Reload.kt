package com.milkfoam.mcgocqhttp.command.subcommand

import com.milkfoam.mcgocqhttp.McGoCqHttp.readConfig
import com.milkfoam.mcgocqhttp.McGoCqHttp.runBot
import com.milkfoam.mcgocqhttp.utils.sendInfoMsg
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.subCommand
import taboolib.platform.util.sendLang

object Reload {

    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            readConfig()
            sender.sendLang("reload-config")
        }
    }

}