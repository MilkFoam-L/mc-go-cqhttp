package com.milkfoam.mcgocqhttp.update

import com.alibaba.fastjson2.JSON
import taboolib.common.LifeCycle
import taboolib.common.platform.SkipTo
import taboolib.common.platform.function.console
import taboolib.common.platform.function.pluginVersion
import taboolib.common.util.Version
import taboolib.module.lang.sendLang
import java.io.BufferedInputStream
import java.net.URL
import java.nio.charset.StandardCharsets

//来自
//https://github.com/TrPlugins/TrChat/blob/v2/project/common/src/main/kotlin/me/arasple/mc/trchat/module/internal/service/Updater.kt
@SkipTo(LifeCycle.LOAD)
object Updater {

    private const val api_url = "https://api.github.com/repos/MilkFoam-L/mc-go-cqhttp/releases/latest"
    private val current_version = Version(pluginVersion)
    private var latest_Version = Version("0.0")
    private var notify = false
    private var information = ""

    fun grabInfo() {
        if (latest_Version.version[0] > 0) {
            return
        }
        kotlin.runCatching {
            URL(api_url).openConnection().also { it.connectTimeout = 30 * 1000; it.readTimeout = 30 * 1000 }
                .getInputStream().use { inputStream ->
                BufferedInputStream(inputStream).use { bufferedInputStream ->
                    val read = bufferedInputStream.readBytes().toString(StandardCharsets.UTF_8)
                    val json = JSON.parseObject(read)!!
                    val latestVersion = json["tag_name"].toString().removePrefix("v")
                    latest_Version = Version(latestVersion)
                    information = json["body"].toString()
                    notifyConsole()
                }
            }
        }
    }

    private fun notifyConsole() {
        if (latest_Version > current_version) {
            console().sendLang("Plugin-Updater-Header", current_version.source, latest_Version.source)
            console().sendMessage(information)
            console().sendLang("Plugin-Updater-Footer")
        } else {
            if (!notify) {
                notify = true
                if (current_version > latest_Version) {
                    console().sendLang("Plugin-Updater-Dev")
                } else {
                    console().sendLang("Plugin-Updater-Latest")
                }
            }
        }
    }


}