package com.milkfoam.mcgocqhttp.utils

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.submit
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptOptions
import taboolib.module.kether.runKether
import taboolib.platform.compat.replacePlaceholder
import taboolib.platform.util.bukkitPlugin

//cmd: say 你好
//op: say 你好
//console: say 你好
//kether: command inline 'say 你好' as op
fun runAction(str: String, player: Player) {
    submit {
        val cmds = extractCommandType(str).replacePlaceholder(player)
        when {
            str.startsWith("op") -> player.runKetherOpCommand(cmds)
            str.startsWith("console") -> player.runKetherConsoleCommand(cmds)
            str.startsWith("cmd") -> player.runKetherPlayerCommand(cmds)
            str.startsWith("kether") -> runKether(cmds, player)
        }
    }
}

// command inline *"chq accept {{ sender }} 新手任务1" as console

fun Player.runKetherOpCommand(cmd: String) {
    runKether("command inline *'$cmd' as op", this)
}

fun Player.runKetherConsoleCommand(cmd: String) {
    runKether("command inline *'$cmd' as console", this)
}

fun Player.runKetherPlayerCommand(cmd: String) {
    runKether("command inline *'$cmd' as player", this)
}

private fun runKether(text: String, player: Player) {
    runKetherList(listOf(text), player)
}

private fun runKetherList(list: List<String>, player: Player) {
    runKether {
        KetherShell.eval(
            list,
            ScriptOptions.builder().namespace(namespace = listOf(bukkitPlugin.name))
                .sender(sender = adaptPlayer(player)).build()
        )
    }
}


private fun extractCommandType(input: String): String {
    val regex = "(cmd|op|console|kether):\\s?(.*)".toRegex()
    val matchResult = regex.find(input)
    if (matchResult?.groupValues?.get(2)?.trimStart() != null) {
        return matchResult.groupValues[2].trimStart()
    } else {
        consoleErrorMsg("&c执行指令出现错误，请检查配置文件!")
    }
    return ""
}