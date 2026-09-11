package com.example.multilang.i18n

import java.util.Locale
import java.util.prefs.Preferences

actual fun systemLanguageCode(): String = Locale.getDefault().language

private const val KEY_LANGUAGE = "language_code"

actual object LanguageStorage {
    private val prefs: Preferences = Preferences.userRoot().node("com/example/multilang")

    actual fun save(languageCode: String) {
        prefs.put(KEY_LANGUAGE, languageCode)
    }

    actual fun load(): String? = prefs.get(KEY_LANGUAGE, null)
}
