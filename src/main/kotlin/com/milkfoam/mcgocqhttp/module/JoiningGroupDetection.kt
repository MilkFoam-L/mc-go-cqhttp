package com.milkfoam.mcgocqhttp.module

import com.milkfoam.mcgocqhttp.McGoCqHttp.configData
import com.milkfoam.mcgocqhttp.McGoCqHttp.webSocketInstance
import com.milkfoam.mcgocqhttp.api.MGCAPI.acceptJoinGroup
import com.milkfoam.mcgocqhttp.api.MGCAPI.rejectJoinGroup
import com.milkfoam.mcgocqhttp.api.event.UserJoinGroupEvent
import com.milkfoam.mcgocqhttp.data.PostData
import com.milkfoam.mcgocqhttp.utils.debugInfo
import taboolib.common.platform.event.SubscribeEvent

object JoiningGroupDetection {

    @SubscribeEvent
    fun starDetection(e: UserJoinGroupEvent) {
        // 获取配置文件
        val postData: PostData = e.postData
        val data = configData?.joiningGroupDetection ?: return debugInfo("入群检测: 未找到配置文件")
        // 判断是否开启
        if (!data.enable) return debugInfo("入群检测: 未开启")
        // 判断是否在群组中
        if (!data.groups.contains(postData.groupId)) return debugInfo("入群检测: 不在群组中")

        data.key.forEach {
            if (postData.comment?.contains(it) == true) {
                debugInfo("理由符合关键词, 自动通过")
                acceptJoinGroup(postData)
                return
            }
        }

        if (data.autoReject) {
            debugInfo("理由不符合关键词, 自动拒绝")
            rejectJoinGroup(postData, data.reason)
            return
        }

    }
}

