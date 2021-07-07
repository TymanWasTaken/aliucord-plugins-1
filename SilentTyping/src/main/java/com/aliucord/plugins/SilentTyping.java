/*
 * Winston's Aliucord Plugins
 * Copyright (C) 2021 WinstonSepruko
 *
 * Licensed under the GNU Lesser General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.aliucord.plugins;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aliucord.Logger;
import com.aliucord.Utils;
import com.aliucord.api.CommandsAPI;
import com.aliucord.entities.Plugin;
import com.discord.api.commands.ApplicationCommandType;
import com.discord.models.commands.ApplicationCommandOption;

import java.util.Collections;
import java.util.List;

import top.canyie.pine.callback.MethodReplacement;

@SuppressWarnings("unused")
public class SilentTyping extends Plugin {
  Logger logger = new Logger("SilentTyping");

  @NonNull
  @Override
  public Manifest getManifest() {
    var manifest = new Manifest();
    manifest.authors = new Manifest.Author[]{new Manifest.Author("winsto", 156990761366192128L)};
    manifest.description = "Hides your typing status from the Discord API, and thus other users. Toggled via a command.";
    manifest.version = "1.1.2";
    manifest.updateUrl = "https://raw.githubusercontent.com/WinstonSepruko/aliucord-plugins/builds/updater.json";
    return manifest;
  }

  @Override
  public void start(Context context) {
    final String className = "com.discord.stores.StoreUserTyping";
    final String methodName = "setUserTyping";
    final Class<?>[] methodArguments = new Class<?>[]{long.class};

    final Runnable unpatch = patcher.patch(className, methodName, methodArguments, MethodReplacement.DO_NOTHING);
    final List<ApplicationCommandOption> options = Collections.singletonList(
        new ApplicationCommandOption(ApplicationCommandType.BOOLEAN, "enabled", "true OR false", null, true, false, null, null)
    );

    commands.registerCommand(
        "silenttyping",
        "Toggle silent typing on or off",
        options,
        ctx -> {
          Boolean enabled = (Boolean) ctx.get("enabled");

          //noinspection ConstantConditions
          settings.setBool("enabled", enabled);

          if (enabled) {
            patcher.patch(className, methodName, methodArguments, MethodReplacement.DO_NOTHING);
            logger.info(Utils.appActivity, "Enabled SilentTyping");
            return new CommandsAPI.CommandResult(null, null, false);
          }
          unpatch.run();
          logger.info(Utils.appActivity, "Disabled SilentTyping");
          return new CommandsAPI.CommandResult(null, null, false);
        }
    );

    if (!settings.getBool("enabled", false)) unpatch.run();
  }

  @Override
  public void stop(Context context) {
    patcher.unpatchAll();
    commands.unregisterAll();
  }
}
