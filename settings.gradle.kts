rootProject.name = "plugins"

listOf(
    "SilentTyping",
    "WebhookTag",
    "NicknameCommand"
).forEach { plugin ->
    include(":$plugin")
    project(":$plugin").projectDir = File(plugin)
}
