package com.aliucord.plugins

import android.content.Context
import com.aliucord.entities.Plugin
import com.aliucord.patcher.PinePatchFn
import com.discord.api.channel.Channel
import top.canyie.pine.Pine

class Dashless : Plugin() {
    override fun getManifest() =
        Manifest().apply {
            authors = arrayOf(Manifest.Author("Nat", 156990761366192128L))
            description =
                "Replaces dashes in channel names with spaces. This is likely to break on Discord updates!"
            version = "1.0.0"
            updateUrl =
                "https://raw.githubusercontent.com/NatSepruko/aliucord-plugins/builds/updater.json"
        }
    
    override fun start(context: Context?) {
        patcher.patch(
            Channel::class.java.getDeclaredMethod("m"),
            PinePatchFn { callFrame: Pine.CallFrame ->
                callFrame.result = callFrame.result.toString().replace("-", " ")
            })
    }
    
    override fun stop(context: Context?) = patcher.unpatchAll()
}