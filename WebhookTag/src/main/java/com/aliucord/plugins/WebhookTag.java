package com.aliucord.plugins;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aliucord.Logger;
import com.aliucord.entities.Plugin;
import com.aliucord.patcher.PinePatchFn;
import com.discord.api.user.User;
import com.discord.models.message.Message;
import com.discord.models.user.CoreUser;
import com.discord.widgets.chat.list.adapter.WidgetChatListAdapterItemMessage;

@SuppressWarnings("unused")
public class WebhookTag extends Plugin {
  public Logger logger = new Logger("WebhookTags");

  @NonNull
  @Override
  public Manifest getManifest() {
    var manifest = new Manifest();
    manifest.authors = new Manifest.Author[] {new Manifest.Author("Nat Sepruko", 156990761366192128L) };
    manifest.description = "Sets the text of the Bot tag on webhooks to \"WEBHOOK\"";
    manifest.version = "1.0.1";
    manifest.updateUrl = "https://raw.githubusercontent.com/NatSepruko/aliucord-plugins/builds/updater.json";
    return manifest;
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void start(Context context) throws Throwable {
    var tag = WidgetChatListAdapterItemMessage.class.getDeclaredField("itemTag");
    tag.setAccessible(true);

    patcher.patch(WidgetChatListAdapterItemMessage.class, "configureItemTag", new Class<?>[] { Message.class }, new PinePatchFn(callFrame -> {
      Message msg = (Message) callFrame.args[0];
      User author = msg.getAuthor();
      CoreUser coreUser = new CoreUser(author);

      try {
        TextView textView = (TextView) tag.get(callFrame.thisObject);
        if (msg.getWebhookId() != null && coreUser.getDiscriminator() == 0 && textView != null) textView.setText("WEBHOOK");
      } catch(Throwable e) {
        logger.error("Error setting tag to WEBHOOK", e);
      }
    }));
  }

  @Override
  public void stop(Context context) {
    patcher.unpatchAll();
  }
}
