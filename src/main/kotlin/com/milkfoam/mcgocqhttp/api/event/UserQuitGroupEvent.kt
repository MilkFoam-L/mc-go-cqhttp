package com.milkfoam.mcgocqhttp.api.event

import com.milkfoam.mcgocqhttp.data.PostData
import taboolib.platform.type.BukkitProxyEvent

// quitType:String | leave、kick、kick_me 分别表示 主动退群、成员被踢、登录号被踢
class UserQuitGroupEvent(
    val postData: PostData,
) : BukkitProxyEvent()