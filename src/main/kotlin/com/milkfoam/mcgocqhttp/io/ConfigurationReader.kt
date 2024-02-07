package com.milkfoam.mcgocqhttp.io

import com.milkfoam.mcgocqhttp.data.*
import com.milkfoam.mcgocqhttp.utils.isNotNull
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration

object ConfigurationReader {

    @Config(migrate = true, value = "settings.yml", autoReload = true)
    lateinit var config: Configuration
        private set

    @Config(migrate = true, value = "storage.yml", autoReload = true)
    lateinit var storage: Configuration
        private set

    @Config(migrate = true, value = "module/account-bind.yml", autoReload = true)
    lateinit var accountBind: Configuration
        private set

    @Config(migrate = true, value = "module/check-user.yml", autoReload = true)
    lateinit var checkUser: Configuration
        private set

    @Config(migrate = true, value = "module/console-tool.yml", autoReload = true)
    lateinit var consoleTool: Configuration
        private set

    @Config(migrate = true, value = "module/group-card.yml", autoReload = true)
    lateinit var groupCard: Configuration
        private set

    @Config(migrate = true, value = "module/image-keyword.yml", autoReload = true)
    lateinit var imageKeyword: Configuration
        private set

    @Config(migrate = true, value = "module/message-connect.yml", autoReload = true)
    lateinit var messageConnect: Configuration
        private set

    @Config(migrate = true, value = "module/message-keyword.yml", autoReload = true)
    lateinit var messageKeyword: Configuration
        private set

    @Config(migrate = true, value = "module/whitelist.yml", autoReload = true)
    lateinit var whitelist: Configuration
        private set

    @Config(migrate = true, value = "module/joining-group-detection.yml", autoReload = true)
    lateinit var joiningGroupDetection: Configuration
        private set

    /**
     * 读取所有模块配置
     * @return ConfigurationData
     */
    fun readModule(): ConfigurationData {
        return ConfigurationData(
            config.getString("version").isNotNull(),
            config.getBoolean("debug"),
            config.getBoolean("check-update"),
            config.getBoolean("auto-reconnect"),
            storage.getBoolean("SQL.enable"),
            config.getBoolean("rcon.enable"),
            config.getString("rcon.host").isNotNull(),
            config.getInt("rcon.port"),
            config.getString("rcon.password").isNotNull(),
            readHttpConnect(),
            readAccountBind(),
            readWhitelist(),
            readGroupCard(),
            readMessageConnect(),
            readConsoleTool(),
            readMessageKeyword(),
            readCheckUser(),
            readImageKeyword(),
            joiningGroupDetection()
        )
    }


    /**
     * 读取加群检测模块配置
     * @return JoiningGroupDetectionModule
     */
    private fun joiningGroupDetection(): JoiningGroupDetectionModule {
        return JoiningGroupDetectionModule(
            joiningGroupDetection.getBoolean("enable"),
            joiningGroupDetection.getStringList("groups").toMutableList(),
            joiningGroupDetection.getStringList("keys").toMutableList(),
            joiningGroupDetection.getBoolean("autoReject.enable"),
            joiningGroupDetection.getString("autoReject.reason").isNotNull(),
        )
    }

    /**
     * 读取http连接模块配置
     * @return HttpConnectConfig
     */
    private fun readHttpConnect(): WebSocketModule {
        return WebSocketModule(
            config.getString("web-socket").isNotNull(),
            config.getBoolean("authToken.enable"),
            config.getString("authToken.value").isNotNull()
        )
    }

    /**
     * 读取白名单模块配置
     * @return BindModule
     */
    private fun readAccountBind(): AccountBindModule {
        return AccountBindModule(
            accountBind.getBoolean("enable"),
            accountBind.getStringList("groups").toMutableList(),
            accountBind.getString("key").isNotNull(),
            accountBind.getLong("expiration-time"),
            accountBind.getInt("lowest-level"),
            accountBind.getStringList("actions").toMutableList()
        )
    }

    /**
     * 读取检查模块配置
     * @return CheckUserModule
     */
    private fun readCheckUser(): CheckUserModule {
        return CheckUserModule(
            checkUser.getBoolean("enable"),
            checkUser.getStringList("groups").toMutableList()
        )
    }

    /**
     * 读取控制台工具模块配置
     * @return ConsoleToolModule
     */
    private fun readConsoleTool(): ConsoleToolModule {
        return ConsoleToolModule(
            consoleTool.getBoolean("enable"),
            consoleTool.getString("keyword").isNotNull(),
            consoleTool.getStringList("admins").toMutableList()
        )
    }

    /**
     * 读取群名片模块配置
     * @return GroupCardModule
     */
    private fun readGroupCard(): GroupCardModule {
        return GroupCardModule(
            groupCard.getBoolean("enable"),
            groupCard.getStringList("groups").toMutableList(),
            groupCard.getString("format").isNotNull()
        )
    }

    /**
     * 读取图片关键词模块配置
     * @return ImageKeywordModule
     */
    private fun readImageKeyword(): ImageKeywordModule {
        val keys: MutableList<ImageKeyWord> = mutableListOf()
        imageKeyword.getConfigurationSection("keys")?.getKeys(false)?.forEach { key ->
            keys.add(
                ImageKeyWord(
                    key,
                    imageKeyword.getString("keys.$key.url").isNotNull(),
                )
            )
        }
        return ImageKeywordModule(
            imageKeyword.getBoolean("enable"),
            imageKeyword.getStringList("groups").toMutableList(),
            keys
        )
    }

    /**
     * 读取消息连接模块配置
     * @return MessageConnectModule
     */
    private fun readMessageConnect(): MessageConnectModule {
        return MessageConnectModule(
            messageConnect.getBoolean("enable"),
            messageConnect.getStringList("groups").toMutableList(),

            messageConnect.getBoolean("mc-to-qq.key-word.enable"),
            messageConnect.getString("mc-to-qq.key-word.text").isNotNull(),
            messageConnect.getString("mc-to-qq.text").isNotNull(),

            messageConnect.getBoolean("qq-to-mc.key-word.enable"),
            messageConnect.getString("qq-to-mc.key-word.text").isNotNull(),
            messageConnect.getString("qq-to-mc.text").isNotNull()
        )
    }

    /**
     * 读取消息关键词模块配置
     */
    private fun readMessageKeyword(): MessageKeyModule {
        val keys: MutableList<MessageKeyword> = mutableListOf()
        messageKeyword.getConfigurationSection("keys")?.getKeys(false)?.forEach { key ->
            keys.add(
                MessageKeyword(
                    key,
                    messageKeyword.getBoolean("keys.$key.is-console-cmd.enable"),
                    messageKeyword.getString("keys.$key.is-console-cmd.command").isNotNull(),
                    messageKeyword.getStringList("keys.$key.is-console-cmd.request").toMutableList(),
                    messageKeyword.getStringList("keys.$key.customMessage").toMutableList()
                )
            )
        }
        return MessageKeyModule(
            messageKeyword.getBoolean("enable"),
            messageKeyword.getStringList("groups").toMutableList(),
            keys
        )
    }

    /**
     * 读取白名单模块配置
     */
    private fun readWhitelist(): WhiteModule {
        return WhiteModule(
            whitelist.getBoolean("enable"),
            whitelist.getBoolean("cancelJoin.enable"),
            whitelist.getLong("cancelJoin.kickTime"),
            whitelist.getBoolean("cancelMove"),
            whitelist.getBoolean("cancelChat"),
            whitelist.getBoolean("cancelCommand"),
            whitelist.getString("messagesSentAfterBan").isNotNull(),
        )
    }

}