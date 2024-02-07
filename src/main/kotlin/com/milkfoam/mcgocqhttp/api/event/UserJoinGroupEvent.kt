package com.milkfoam.mcgocqhttp.api.event

import com.milkfoam.mcgocqhttp.data.GroupUserData
import com.milkfoam.mcgocqhttp.data.PostData
import com.milkfoam.mcgocqhttp.data.SenderData
import taboolib.platform.type.BukkitProxyEvent

class UserJoinGroupEvent(
    val postData: PostData
) : BukkitProxyEvent()