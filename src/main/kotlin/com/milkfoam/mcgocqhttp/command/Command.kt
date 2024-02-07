package com.milkfoam.mcgocqhttp.command

import com.milkfoam.mcgocqhttp.command.subcommand.Bind
import com.milkfoam.mcgocqhttp.command.subcommand.Connect
import com.milkfoam.mcgocqhttp.command.subcommand.Rcon
import com.milkfoam.mcgocqhttp.command.subcommand.Reload
import taboolib.common.platform.command.*
import taboolib.expansion.createHelper

@CommandHeader("mc-go-cqhttp", ["mgc"])
object Command {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody
    val reload = Reload.reload

    @CommandBody
    val connect = Connect.connect

    @CommandBody
    val rconReload = Rcon.rconReload

    @CommandBody
    val addBind = Bind.addBind

    @CommandBody
    val removeBind = Bind.removeBind

    @CommandBody
    val lookBind = Bind.lookBind

    @CommandBody
    val lookAllBind = Bind.lookAllBind

    @CommandBody(optional = true)
    val removeBindAtAllBind = Bind.removeBindAtAllBind

}