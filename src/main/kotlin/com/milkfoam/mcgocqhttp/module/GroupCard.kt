package com.milkfoam.mcgocqhttp.module

import com.milkfoam.mcgocqhttp.McGoCqHttp
import com.milkfoam.mcgocqhttp.McGoCqHttp.configData
import com.milkfoam.mcgocqhttp.api.MGCAPI
import com.milkfoam.mcgocqhttp.api.event.PlayerBindQQEvent
import com.milkfoam.mcgocqhttp.utils.debugInfo
import com.milkfoam.mcgocqhttp.utils.withNotNull
import taboolib.common.platform.event.SubscribeEvent

object GroupCard {

    @SubscribeEvent
    fun changeCard(e: PlayerBindQQEvent) {

        val data = configData?.groupCard ?: return debugInfo("群名片更改: 未找到配置文件")
        val qq = e.qq
        val player = e.name
        val postData = e.data
        val group = postData.groupId
        val format = data.format.replace("{player}", player)

        debugInfo("群名片更改: 开始")
        debugInfo("群名片更改: qq: $qq")
        debugInfo("群名片更改: player: $player")
        debugInfo("群名片更改: group: $group")
        debugInfo("群名片更改: format: $format")

        // 判断是否开启
        if (!data.enable) return debugInfo("群名片更改: 未开启")
        // 判断是否在群组中
        if (!data.groups.contains(group)) return debugInfo("群名片更改: 不在群组中")

        group.withNotNull {
            MGCAPI.setGroupCard(it, qq, format)
        }

    }

}