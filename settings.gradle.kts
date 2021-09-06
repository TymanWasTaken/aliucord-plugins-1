rootProject.name = "plugins"

listOf(
    "WhichMessage",
    "SilentTyping",
    "WebhookTag",
    "NicknameCommand",
    "Dashless"
).forEach { plugin ->
    include(":$plugin")
    project(":$plugin").projectDir = File("./plugins/$plugin")
}
