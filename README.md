# Winston's Aliucord Plugins

My repository for [Aliucord](https://github.com/Aliucord) plugins.

# List of Plugins

Feel free to browse this list to see a list of my plugins and what they do.

## SilentTyping

Hides your typing status from the Discord API, and thus other users. Toggled via a command.

# Build instructions

Building these plugins requires you do either have the source of [buildtool](https://github.com/Aliucord/buildtool) or the [prebuilt binary](https://github.com/Aliucord/buildtool/releases/latest) as well as a `config.json` in the same folder.

**1.** Create a setup like the one below.

**Example**

```
.
├── repo (Aliucord/Aliucord - cloned)
│
├── aliucord-plugins (this repo - cloned)
│
├── buildsPlugins (output dir for buildtool)
│
├── buildtool (Aliucord/buildtool - cloned) OR buildtool (prebuilt binary)
│
└── config.json (modified version of the config.example.json you grabbed earlier)
```

**2.** Configure `config.json` for `buildtool` so that it suits your working environment.

**Example** _remember, if you're on Windows you need to use `\\` for the `androidSDK` key, e.g `C:\\Users\\username`_

```json
{
  "aliucord": "./repo",
  "plugins": "./aliucord-plugins",
  "androidSDK": "Path/To/Android/Sdk",
  "outputs": "./builds",
  "outputsPlugins": "./buildsPlugins"
}
```

**2.** Run `buildtool`...

```
.\buildtool -p <PLUGIN_NAME>
```
e.g
```
.\buildtool -p SilentTyping
```

**3.** Find the built plugin(s) in `./buildsPlugins`.