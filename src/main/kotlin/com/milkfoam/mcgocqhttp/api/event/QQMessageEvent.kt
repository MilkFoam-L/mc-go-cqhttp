package com.milkfoam.mcgocqhttp.api.event

import com.milkfoam.mcgocqhttp.data.PostData
import taboolib.platform.type.BukkitProxyEvent

class QQMessageEvent(
    val jsonString: String,
    val data: PostData
) : BukkitProxyEvent()