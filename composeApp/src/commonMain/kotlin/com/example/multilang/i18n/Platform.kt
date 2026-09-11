package com.example.multilang.i18n

/** The device/OS language at first launch, as a two-letter code (e.g. "en", "de", "fr"). */
expect fun systemLanguageCode(): String

/** Tiny key-value store for remembering the user's chosen language across restarts. */
expect object LanguageStorage {
    fun save(languageCode: String)
    fun load(): String?
}
