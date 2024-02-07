package com.milkfoam.mcgocqhttp.module

import com.milkfoam.mcgocqhttp.McGoCqHttp
import com.milkfoam.mcgocqhttp.McGoCqHttp.configData
import com.milkfoam.mcgocqhttp.api.MGCAPI
import com.milkfoam.mcgocqhttp.api.event.QQMessageEvent
import com.milkfoam.mcgocqhttp.data.PostData
import com.milkfoam.mcgocqhttp.service.request.GroupMessage
import com.milkfoam.mcgocqhttp.utils.debugInfo
import taboolib.common.platform.event.SubscribeEvent

object ImageKeyword {

    @SubscribeEvent
    fun listening(e: QQMessageEvent) {
        // 获取消息
        val message = e.data.rawMessage
        // 获取配置文件
        val postData: PostData = e.data
        // 获取配置文件
        val data = configData?.imageKeyWord ?: return debugInfo("图片关键词: 未找到配置文件")
        // 获取群组
        val group = postData.groupId
        // 判断是否开启
        if (!data.enable) return debugInfo("图片关键词: 未开启")
        // 判断是否在群组中
        if (!data.groups.contains(group)) return debugInfo("图片关键词: 不在群组中")
        // 判断是否为空
        if (message == null || group == null) return debugInfo("图片关键词: 未找到消息或群组")

        data.keys.forEach {
            if (message.contains(it.key)) {
                MGCAPI.sendGroupMessage(group, MGCAPI.getUrlImage(it.url))
            }
        }


    }


}