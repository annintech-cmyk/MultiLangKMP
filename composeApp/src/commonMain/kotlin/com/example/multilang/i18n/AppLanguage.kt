package com.example.multilang.i18n

/**
 * The set of languages this app ships translations for.
 * Adding a new language means: add a case here, then implement [Strings] for it
 * in a new StringsXx object, then register it in [stringsFor] — the compiler
 * won't let you forget a key because [Strings] is a plain interface.
 */
enum class AppLanguage(val code: String, val nativeName: String, val flag: String) {
    ENGLISH("en", "English", "🇬🇧"),
    GERMAN("de", "Deutsch", "🇩🇪"),
    FRENCH("fr", "Français", "🇫🇷");

    companion object {
        val default = ENGLISH

        fun fromCode(code: String?): AppLanguage =
            entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: default
    }
}
