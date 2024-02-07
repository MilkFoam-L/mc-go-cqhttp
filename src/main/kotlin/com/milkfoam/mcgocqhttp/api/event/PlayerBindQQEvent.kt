package com.milkfoam.mcgocqhttp.api.event

import com.milkfoam.mcgocqhttp.data.PostData
import taboolib.platform.type.BukkitProxyEvent

class PlayerBindQQEvent(
    val qq: String,
    val name: String,
    val data: PostData
) : BukkitProxyEvent()