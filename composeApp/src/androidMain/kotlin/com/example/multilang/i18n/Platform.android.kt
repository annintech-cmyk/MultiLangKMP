package com.example.multilang.i18n

import android.content.Context
import com.example.multilang.AppContext
import java.util.Locale

actual fun systemLanguageCode(): String = Locale.getDefault().language

private const val PREFS_NAME = "multilang_prefs"
private const val KEY_LANGUAGE = "language_code"

actual object LanguageStorage {
    private val prefs
        get() = AppContext.instance.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    actual fun save(languageCode: String) {
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply()
    }

    actual fun load(): String? = prefs.getString(KEY_LANGUAGE, null)
}
