package com.example.multilang.i18n

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Register a new language here after adding its StringsXx object.
 * This is the only `when` in the app that must stay exhaustive over
 * [AppLanguage] — the compiler errors if a case is missing.
 */
fun stringsFor(language: AppLanguage): Strings = when (language) {
    AppLanguage.ENGLISH -> StringsEn
    AppLanguage.GERMAN -> StringsDe
    AppLanguage.FRENCH -> StringsFr
}

/**
 * Read with `LocalStrings.current` anywhere inside the composable tree.
 * There is deliberately no default that silently falls back to English at
 * the call site — [com.example.multilang.App] always provides a real value,
 * so a missing provider fails loudly instead of showing wrong text.
 */
val LocalStrings = staticCompositionLocalOf<Strings> {
    error("No Strings provided — wrap content in CompositionLocalProvider(LocalStrings provides ...)")
}
