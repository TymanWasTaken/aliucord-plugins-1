package com.aliucord.plugins

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.PinePatchFn
import com.aliucord.plugins.whichmessage.Decorator
import com.aliucord.plugins.whichmessage.RecyclerAdapter
import com.aliucord.plugins.whichmessage.ViewsBuilder
import com.aliucord.utils.DimenUtils
import com.discord.utilities.color.ColorCompat
import com.discord.widgets.chat.list.actions.WidgetChatListActions
import com.lytefast.flexinput.R

@AliucordPlugin
class WhichMessage : Plugin() {
    private val viewId = View.generateViewId()
    
    override fun start(context: Context?) {
        patcher.patch(WidgetChatListActions::class.java.getDeclaredMethod(
            "configureUI",
            WidgetChatListActions.Model::class.java
        ),
            PinePatchFn {
                val msg = (it.args[0] as WidgetChatListActions.Model).message
                if (msg.isLoading) return@PinePatchFn
                
                val layout =
                    ((it.thisObject as WidgetChatListActions).requireView() as NestedScrollView).getChildAt(
                        0
                    ) as LinearLayout
                if (layout.findViewById<LinearLayout>(viewId) != null) return@PinePatchFn
                val p = DimenUtils.getDefaultPadding()
                
                val msgView = RecyclerView(layout.context)
                    .apply {
                        setPadding(p, p, p, p)
                        adapter = RecyclerAdapter(ViewsBuilder(layout.context, msg).getViews())
                        layoutManager =
                            LinearLayoutManager(layout.context, RecyclerView.VERTICAL, false)
                        addItemDecoration(Decorator(p / 4))
                        id = viewId
                        setBackgroundColor(
                            ColorCompat.getThemedColor(
                                layout.context,
                                R.b.colorBackgroundTertiary
                            )
                        )
                    }
                
                layout.addView(msgView, 0)
            })
    }
    
    override fun stop(context: Context?) = patcher.unpatchAll()
}