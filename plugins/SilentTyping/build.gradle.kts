version = "1.2.1"
description =
    "Hides your typing status from the Discord API, and thus other users. Toggled via a command."

aliucord {
    changelog.set(
        """
        Improved {improved marginTop}
        ======================
      
        * Command now doesn't set the setting if the value is the same as what is given.
        * Command now gives the user better feedback if they try to change the setting to the already set value.
    """.trimIndent()
    )
}