package com.aliucord.plugins

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.aliucord.Logger
import com.aliucord.entities.Plugin
import com.aliucord.entities.Plugin.Manifest.Author
import com.aliucord.patcher.PinePatchFn
import com.discord.api.user.User
import com.discord.models.message.Message
import com.discord.widgets.chat.list.adapter.WidgetChatListAdapterItemMessage
import top.canyie.pine.Pine.CallFrame
import com.discord.models.user.CoreUser


class WebhookTag : Plugin() {
    var logger = Logger("WebhookTags")

    override fun getManifest() =
        Manifest().apply {
            authors = arrayOf(Author("Nat", 156990761366192128L))
            description = "Replaces the \"BOT\" text with \"WEBHOOK\" on webhooks."
            version = "1.1.1"
            updateUrl = "https://raw.githubusercontent.com/NatSepruko/aliucord-plugins/builds/updater.json"
            changelog =
                """
                    Improved {improved marginTop}
                    ======================
                    
                    * Migrated to Kotlin because it's better
                    * Updated the plugin description
                    
                    Fixed {fixed marginTop}
                    ======================
                    
                    * Re-add null-check because I didn't quite know how null-checking worked with Kotlin in this situation
                """.trimIndent()
        }

    @SuppressLint("SetTextI18n")
    override fun start(context: Context?) {
        patcher.patch(
            WidgetChatListAdapterItemMessage::class.java,
            "configureItemTag",
            arrayOf(Message::class.java),
            PinePatchFn{ callFrame: CallFrame ->
                val msg = callFrame.args[0] as Message
                val author = msg.author as User
                val coreUser = CoreUser(author)

                val tag = callFrame.thisObject.javaClass
                    .getDeclaredField("itemTag")
                    .let {
                        it.isAccessible = true
                        it.get(callFrame.thisObject) as TextView?
                    }

                try {
                    if (msg.webhookId != null && coreUser.discriminator == 0 && tag != null) tag.text = "WEBHOOK"
                } catch (e: Throwable) {
                    logger.error("Failed to set text on webhook tag", e)
                }
            })
    }

    override fun stop(context: Context?) {
        patcher.unpatchAll()
    }

}