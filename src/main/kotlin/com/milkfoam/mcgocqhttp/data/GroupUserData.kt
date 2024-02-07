package com.milkfoam.mcgocqhttp.data

data class GroupUserData(
    val age: Int,
    val area: String,
    val card: String,
    val cardChangeable: Boolean,
    val groupId: Long,
    val joinTime: Long,
    val lastSentTime: Long,
    val level: String,
    val nickname: String,
    val role: String,
    val sex: String,
    val shutUpTimestamp: Long,
    val title: String,
    val titleExpireTime: Long,
    val unfriendly: Boolean,
    val userId: Long
)
