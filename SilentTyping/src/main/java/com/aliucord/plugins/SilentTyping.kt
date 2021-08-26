package com.aliucord.plugins

import android.content.Context
import com.aliucord.Logger
import com.aliucord.Utils
import com.aliucord.api.CommandsAPI.CommandResult
import com.aliucord.entities.CommandContext
import com.aliucord.entities.Plugin
import com.aliucord.entities.Plugin.Manifest.Author
import com.discord.models.commands.ApplicationCommandOption
import com.discord.api.commands.ApplicationCommandType
import top.canyie.pine.callback.MethodReplacement

class SilentTyping : Plugin() {
    private val logger = Logger("SilentTyping")
    
    override fun getManifest() =
        Manifest().apply {
            authors = arrayOf(Author("Nat Sepruko", 156990761366192128L))
            description = "Hides your typing status from the Discord API, and thus other users. Toggled via a command."
            version = "v1.2.0"
            updateUrl = "https://raw.githubusercontent.com/NatSepruko/aliucord-plugins/builds/updater.json"
            changelog =
                """
                    Improved {improved marginTop}
                    ======================
                    
                    * Migrated to Kotlin because it's better
                    * Enabled is now the default, hopefully stopping some people from complaining that it "isn't working"
                """.trimIndent()
        }
    
    override fun start(context: Context?) {
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
            "silenttyping",
            "Toggle silent typing on or off",
            listOf(opt)
        ) { ctx: CommandContext ->
            val enabled = ctx.getBool("enabled")
            
            if (enabled!!) {
                patcher.patch(
                    "com.discord.stores.StoreUserTyping",
                    "setUserTyping",
                    arrayOf<Class<*>>(Long::class.java),
                    MethodReplacement.DO_NOTHING
                ); logger.info(Utils.appActivity, "Enabled SilentTyping")
    
                settings.setBool("enabled", enabled)
                
                return@registerCommand CommandResult(null, null, false)
            }
            unpatch.run(); logger.info(Utils.appActivity, "Disabled SilentTyping")
            
            settings.setBool("enabled", enabled)
            
            return@registerCommand CommandResult(null, null, false)
        }
        
        if (!settings.getBool("enabled", true)) unpatch.run()
    }
    
    override fun stop(context: Context?) {
        patcher.unpatchAll(); commands.unregisterAll()
    }
}