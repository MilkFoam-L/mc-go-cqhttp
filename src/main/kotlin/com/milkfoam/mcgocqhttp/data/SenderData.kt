package com.milkfoam.mcgocqhttp.data

import com.alibaba.fastjson2.annotation.JSONField

data class SenderData(
    val age: Int?,
    val area: String?,
    val card: String?,
    val level: Int?,
    @JSONField(name = "nickname")
    val nickname: String?,
    val role: String?,
    val sex: String?,
    val title: String?,
    @JSONField(name = "user_id")
    val userId: Long?,

    val sign: String?,
    val vipLevel: String?
)
