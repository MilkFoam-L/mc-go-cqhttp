package com.milkfoam.mcgocqhttp.utils

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.OfflinePlayer

inline fun <T : Any, R> T?.withNotNull(block: (T) -> R): R? {
    return this?.let(block)
}

inline fun <T> T.applyIf(condition: Boolean, block: T.() -> Unit): T {
    return if (condition) {
        this.apply(block)
    } else {
        this
    }
}

fun String?.isNotNull(): String {
    return this ?: "值为空!"
}

fun String?.replace(oldValue: String): String {
    return this?.replace(oldValue, "") ?: "值为空!"
}

fun String?.replacePAPI(player: OfflinePlayer): String {
    if (this == null) return "值为空!"
    return PlaceholderAPI.setPlaceholders(player, this)
}

fun replaceMessageTag(input: String): String {
    val tag = extractCQType(input)
    when (tag) {
        MessageCQTag.FACE -> {
            val pattern = """\[CQ:face,[^]]*]"""
            return input.replace(Regex(pattern), "[表情]")
        }

        MessageCQTag.RECORD -> {
            val pattern = """\[CQ:record,[^]]*]"""
            return input.replace(Regex(pattern), "[语音]")
        }

        MessageCQTag.VIDEO -> {
            val pattern = """\[CQ:video,[^]]*]"""
            return input.replace(Regex(pattern), "[短视频]")
        }

        MessageCQTag.AT -> {
            val pattern = """\[CQ:at,[^]]*]"""
            return input.replace(Regex(pattern), "[at]")
        }

        MessageCQTag.IMAGE -> {
            val pattern = """\[CQ:image,[^]]*]"""
            return input.replace(Regex(pattern), "[图片]")
        }

        MessageCQTag.REDBAG -> {
            val pattern = """\[CQ:redbag,[^]]*]"""
            return input.replace(Regex(pattern), "[红包]")
        }

        else -> {
            return input
        }
    }
}

fun extractCQType(input: String): MessageCQTag? {
    val pattern = """\[CQ:(\w+),""".toRegex()
    return pattern.find(input)?.groupValues?.get(1)?.let {
        MessageCQTag.valueOf(it)
    }
}

enum class MessageCQTag(name: String) {
    FACE("表情"),
    RECORD("语音"),
    VIDEO("短视频"),
    AT("at"),
    IMAGE("图片"),
    REDBAG("红包"),
}