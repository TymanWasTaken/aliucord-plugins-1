rootProject.name = "plugins"

listOf(
    "SilentTyping",
    "WebhookTag",
    "NicknameCommand",
    "Dashless"
).forEach { plugin ->
    include(":$plugin")
    project(":$plugin").projectDir = File(plugin)
}
