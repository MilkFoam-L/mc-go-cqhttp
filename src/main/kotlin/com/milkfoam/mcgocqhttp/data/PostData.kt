package com.milkfoam.mcgocqhttp.data

import com.alibaba.fastjson2.annotation.JSONField


data class PostData(

    val time: String?,//发送时间
    @JSONField(name = "self_id")
    val selfId: String?,//机器人本身id
    @JSONField(name = "post_type")
    val postType: String?,//上报类型

    @JSONField(name = "request_type")
    val requestType: String?,//请求类型
    @JSONField(name = "message_type")
    val messageType: String?,//消息类型
    @JSONField(name = "sub_type")
    val subType: String?,//提交类型  加群（add 或 invite, 请求类型（需要和上报消息中的 sub_type 字段相符））
    @JSONField(name = "message_id")
    val messageId: String?,//消息id
    @JSONField(name = "user_id")
    val userId: String?,//发送者id
    @JSONField(name = "target_id")
    val targetId: String?,//响应id
    @JSONField(name = "raw_message")
    val rawMessage: String?,//真实消息
    @JSONField(name = "group_id")
    val groupId: String?,//群号
    @JSONField(name = "notice_type")
    val noticeType: String?,//加群验证消息

    val comment: String?,//加群验证消息
    val font: String?,//字体
    val message: String?,//消息
    val flag: String?,//加群请求的 flag（需从上报的数据中获得）

    @JSONField(name = "_post_method")
    val postMethod: String?,//上报方式
    @JSONField(name = "meta_event_type")
    val metaEventType: String?,//元事件类型

    @JSONField(name = "sender")
    val sender: SenderData?

)