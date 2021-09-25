package com.aliucord.plugins

import android.content.Context
import com.aliucord.Logger
import com.aliucord.Utils
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.api.CommandsAPI.CommandResult
import com.aliucord.entities.CommandContext
import com.aliucord.entities.Plugin
import com.discord.api.commands.ApplicationCommandType
import com.discord.models.commands.ApplicationCommandOption
import com.discord.stores.StoreUserTyping
import top.canyie.pine.callback.MethodReplacement

@AliucordPlugin
class SilentTyping : Plugin() {
    private val logger = Logger("SilentTyping")

    override fun start(context: Context?) {
        val enabled = settings.getBool("enabled", true)
        val unpatch: Runnable = patcher.patch(
            StoreUserTyping::class.java.getDeclaredMethod("setUserTyping", Long::class.java),
            MethodReplacement.DO_NOTHING
        )
        val opt = ApplicationCommandOption(
            ApplicationCommandType.BOOLEAN,
            "enabled",
            "true OR false",
            null,
            true,
            false,
            null,
            null
        )

        commands.registerCommand(
            "silenttyping",
            "Toggle silent typing on or off",
            listOf(opt)
        ) { ctx: CommandContext ->
            val arg = ctx.getBool("enabled")

            if (arg!! != enabled) {
                settings.setBool("enabled", arg)

                if (arg) {
                    patcher.patch(
                        StoreUserTyping::class.java.getDeclaredMethod(
                            "setUserTyping",
                            Long::class.java
                        ),
                        MethodReplacement.DO_NOTHING
                    )
                    logger.info(Utils.appActivity, "Enabled SilentTyping")
                } else {
                    unpatch.run()
                    logger.info(Utils.appActivity, "Disabled SilentTyping")
                }
            } else {
                logger.info(Utils.appActivity, "SilentTyping status was not changed!")
            }

            return@registerCommand CommandResult(null, null, false)
        }

        if (!settings.getBool("enabled", true)) unpatch.run()
    }

    override fun stop(context: Context?) {
        patcher.unpatchAll(); commands.unregisterAll()
    }
}