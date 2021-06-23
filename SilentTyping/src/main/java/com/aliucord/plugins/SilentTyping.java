package com.aliucord.plugins;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aliucord.entities.Plugin;

import top.canyie.pine.callback.MethodReplacement;

@SuppressWarnings("ununsed")
public class SilentTyping extends Plugin {
  @NonNull
  @Override
  public Manifest getManifest() {
    var manifest = new Manifest();
    manifest.authors = new Manifest.Author[]{new Manifest.Author("winsto", 156990761366192128L)};
    manifest.description = "Hides your typing status from the Discord API, and thus other users.";
    manifest.version = "1.0.0";
    manifest.updateUrl = "https://raw.githubusercontent.com/WinstonSepruko/aliucord-plugins/builds/updater.json";
    return manifest;
  }

  @Override
  public void start(Context context) throws Throwable {
    var className = "com.discord.stores.StoreUserTyping";
    var methodName = "setUserTyping";
    var methodArguments = new Class<?>[] {long.class};

    patcher.patch(className, methodName, methodArguments, MethodReplacement.DO_NOTHING);
  }

  @Override
  public void stop(Context context) {
    patcher.unpatchAll();
  }
}
