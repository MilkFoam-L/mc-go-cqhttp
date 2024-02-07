package com.milkfoam.mcgocqhttp.io


import taboolib.module.lang.Language
import taboolib.module.lang.TypeText

object LanguageReader {

    val notFindPlayer = (Language.languageFile["zh_CN"]!!.nodes["QQ-notFindPlayer"] as TypeText).text!!
    val sendMessageToMinecraft =
        (Language.languageFile["zh_CN"]!!.nodes["QQ-sendMessageToMinecraft"] as TypeText).text!!
    val success = (Language.languageFile["zh_CN"]!!.nodes["QQ-Success"] as TypeText).text!!
    val timeOut = (Language.languageFile["zh_CN"]!!.nodes["QQ-TimeOut"] as TypeText).text!!
    val isBind = (Language.languageFile["zh_CN"]!!.nodes["QQ-isBind"] as TypeText).text!!
    val isInBind = (Language.languageFile["zh_CN"]!!.nodes["QQ-isInBind"] as TypeText).text!!
    val levelIsNotStandard =
        (Language.languageFile["zh_CN"]!!.nodes["QQ-levelIsNotStandard"] as TypeText).text!!

    val bindKey = (Language.languageFile["zh_CN"]!!.nodes["Minecraft-BindKey"] as TypeText).text!!

    val keyWordNotBnd = (Language.languageFile["zh_CN"]!!.nodes["QQ-keyWordNotBnd"] as TypeText).text!!

    val isInBindButLeave =
        (Language.languageFile["zh_CN"]!!.nodes["QQ-isInBindButLeave"] as TypeText).text!!

}