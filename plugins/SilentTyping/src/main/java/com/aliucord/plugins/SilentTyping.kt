package com.aliucord.plugins

import android.content.Context
import com.aliucord.Logger
import com.aliucord.Utils
import com.aliucord.api.CommandsAPI.CommandResult
import com.aliucord.entities.CommandContext
import com.aliucord.entities.Plugin
import com.aliucord.entities.Plugin.Manifest.Author
import com.discord.api.commands.ApplicationCommandType
import com.discord.models.commands.ApplicationCommandOption
import top.canyie.pine.callback.MethodReplacement

class SilentTyping : Plugin() {
    private val logger = Logger("SilentTyping")
    
    override fun getManifest() =
        Manifest().apply {
            authors = arrayOf(Author("Nat", 156990761366192128L))
            description =
                "Hides your typing status from the Discord API, and thus other users. Toggled via a command."
            version = "1.2.1"
            updateUrl =
                "https://raw.githubusercontent.com/NatSepruko/aliucord-plugins/builds/updater.json"
            changelog =
                """
                    Improved {improved marginTop}
                    ======================
                    
                    * Command now doesn't set the setting if the value is the same as what is given.
                    * Command now gives the user better feedback if they try to change the setting to the already set value.
                """.trimIndent()
        }
    
    override fun start(context: Context?) {
        val enabled = settings.getBool("enabled", true)
        val unpatch: Runnable = patcher.patch(
            "com.discord.stores.StoreUserTyping",
            "setUserTyping",
            arrayOf<Class<*>>(Long::class.java),
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
            this.name.lowercase(),
            "Toggle silent typing on or off",
            listOf(opt)
        ) { ctx: CommandContext ->
            val arg = ctx.getBool("enabled")
            
            if (arg!! != enabled) {
                settings.setBool("enabled", arg)
                
                if (arg) {
                    patcher.patch(
                        "com.discord.stores.StoreUserTyping",
                        "setUserTyping",
                        arrayOf<Class<*>>(Long::class.java),
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