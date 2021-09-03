# Nat's Aliucord Plugins

My repository for [Aliucord](https://github.com/Aliucord) plugins.

## List of Plugins

Feel free to browse this list to see a list of my plugins and what they do.

### SilentTyping

Hides your typing status from the Discord API, and thus other users. Toggled via a command.

### WebhookTag

Sets the tag text on webhook messages to "WEBHOOK" instead of "BOT"

### NicknameCommand

Allows you to set your nickname in a server via a command. It does not yet check if you have permission to do so (coming soon™️)

### Dashless

Replaces dashes in channel names with spaces, this will likely break on Discord updates so watch out (if it does it probably won't crash, but channels might be named something crazy)! 

## Build instructions

**1.** Clone this repository and change directory to the cloned repository.

**2.** Build plugins, preferably by name using `./gradlew PluginName:make` or deploy immediately to a device connected via ADB with `./gradlew PluginName:deployWithAdb` where `PluginName` is the name of the plugin you'd like to build, e.g `SilentTyping`.

> *Psst! The `make` task will output the build to the `PluginName/build` directory!*