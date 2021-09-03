package com.aliucord.plugins

import android.content.Context
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.api.CommandsAPI
import com.aliucord.entities.CommandContext
import com.aliucord.entities.Plugin
import com.aliucord.utils.RxUtils.subscribe
import com.discord.api.commands.ApplicationCommandType
import com.discord.models.commands.ApplicationCommandOption
import com.discord.restapi.RestAPIParams
import com.discord.utilities.rest.RestAPI
import rx.Subscriber
import java.util.*

@AliucordPlugin
class NicknameCommand : Plugin() {
    override fun start(context: Context?) {
        val opt = listOf(
            ApplicationCommandOption(
                ApplicationCommandType.STRING,
                "new_nick",
                "New nickname",
                null,
                false,
                false,
                null,
                null
            )
        )
        
        commands.registerCommand(
            "nick",
            "Change nickname on this server.",
            opt,
        ) { ctx: CommandContext ->
            val nick = ctx.getString("new_nick")
            val content: String
            
            if (!ctx.channel.isGuild()) {
                content = "This is not a server!"
                return@registerCommand CommandsAPI.CommandResult(content, null, false)
            }
            
            content = setNickname(ctx, nick)
            return@registerCommand CommandsAPI.CommandResult(content, null, false)
        }
    }
    
    override fun stop(context: Context?) = commands.unregisterAll()
}

fun setNickname(ctx: CommandContext, nickname: String?): String {
    var nick = ""
    if (nickname != null) nick = nickname
    
    var content = "Your nickname on this server has been changed to **$nick**."
    val observable =
        RestAPI.getApi().changeGuildNickname(ctx.channel.guildId, RestAPIParams.Nick(nick))
    
    observable.subscribe(object : Subscriber<Void>() {
        override fun onCompleted() {
            if (nick == "") content = "Your nickname on this server has been reset."
        }
        
        override fun onError(e: Throwable) {
            content = if (nick == "") "Failed resetting nickname with error: $e"
            else "Failed setting nickname with error: $e"
        }
        
        override fun onNext(p0: Void?) {
            return
        }
    })
    
    return content
}