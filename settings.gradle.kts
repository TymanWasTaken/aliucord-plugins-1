rootProject.name = "plugins"

listOf(
    "SilentTyping",
    "WebhookTag"
).forEach { plugin ->
    include(":$plugin")
    project(":$plugin").projectDir = File(plugin)
}