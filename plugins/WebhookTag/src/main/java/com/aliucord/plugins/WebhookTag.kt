package com.aliucord.plugins

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.aliucord.Logger
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.PinePatchFn
import com.discord.api.user.User
import com.discord.models.message.Message
import com.discord.models.user.CoreUser
import com.discord.widgets.chat.list.adapter.WidgetChatListAdapterItemMessage
import top.canyie.pine.Pine.CallFrame

@AliucordPlugin
class WebhookTag : Plugin() {
    private val logger = Logger("WebhookTag")
    
    @SuppressLint("SetTextI18n")
    override fun start(context: Context?) {
        patcher.patch(
            WidgetChatListAdapterItemMessage::class.java,
            "configureItemTag",
            arrayOf(Message::class.java),
            PinePatchFn { callFrame: CallFrame ->
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
                    if (msg.webhookId != null && coreUser.discriminator == 0 && tag != null) tag.text =
                        "WEBHOOK"
                } catch (e: Throwable) {
                    logger.error("Failed to set text on webhook tag", e)
                }
            })
    }
    
    override fun stop(context: Context?) {
        patcher.unpatchAll()
    }
    
}