package com.honoka

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info

object HonokaBot : KotlinPlugin(
    JvmPluginDescription(
        id = "com.honoka.honoka-bot",
        version = "1.0-SNAPSHOT",
    )
) {
    override fun onEnable() {
        logger.info { "Plugin loaded" }
    }
}