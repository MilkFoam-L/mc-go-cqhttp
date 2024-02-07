package com.milkfoam.mcgocqhttp.data

data class ConfigurationData(
    val version: String,
    val debug: Boolean,
    val checkUpdate: Boolean,
    val autoReconnect: Boolean,
    val storageMode: Boolean,

    val rconEnable: Boolean,
    val rconHost: String,
    val rconPort: Int,
    val rconPassword: String,

    val webSocketConfig: WebSocketModule,
    val accountBind: AccountBindModule,
    val whiteList: WhiteModule,
    val groupCard: GroupCardModule,
    val messageConnect: MessageConnectModule,
    val consoleTool: ConsoleToolModule,
    val messageKeyword: MessageKeyModule,
    val checkUser: CheckUserModule,
    val imageKeyWord: ImageKeywordModule,
    val joiningGroupDetection: JoiningGroupDetectionModule
)

data class WebSocketModule(
    val webSocket: String,
    val accessToken: Boolean,
    val accessTokenValue: String,
)

data class AccountBindModule(
    val enable: Boolean,
    val groups: MutableList<String>,
    val key: String,
    val expirationTime: Long,
    val lowestLevel: Int,
    val actions: MutableList<String>
)

data class WhiteModule(
    val enable: Boolean,
    val cancelJoinEnable: Boolean,
    val cancelJoinKickTime: Long,
    val cancelMove: Boolean,
    val cancelChat: Boolean,
    val cancelCommand: Boolean,
    val message: String
)

data class GroupCardModule(
    val enable: Boolean,
    val groups: MutableList<String>,
    val format: String
)

data class MessageConnectModule(
    val enable: Boolean,
    val groups: MutableList<String>,
    val mcToQQKeyWordsEnable: Boolean,
    val mcToQQKeyWordsText: String,
    val mcToQQText: String,
    val qQtoMcKeyWordsEnable: Boolean,
    val qQtoMcKeyWordsText: String,
    val qQtoMcText: String,
)

data class ConsoleToolModule(
    val enable: Boolean,
    val keyword: String,
    val admins: MutableList<String>,
)

data class MessageKeyModule(
    val enable: Boolean,
    val groups: MutableList<String>,
    val keys: MutableList<MessageKeyword>
)

data class MessageKeyword(
    val key: String,
    val isConsoleCmd: Boolean?,
    val command: String?,
    val request: MutableList<String>?,
    val customMessage: MutableList<String>?
)

data class CheckUserModule(
    val enable: Boolean,
    val groups: MutableList<String>
)

data class ImageKeywordModule(
    val enable: Boolean,
    val groups: MutableList<String>,
    val keys: MutableList<ImageKeyWord>
)

data class ImageKeyWord(
    val key: String,
    val url: String
)

data class JoiningGroupDetectionModule(
    val enable: Boolean,
    val groups: MutableList<String>,
    val key: MutableList<String>,
    val autoReject: Boolean,
    val reason: String,
)




