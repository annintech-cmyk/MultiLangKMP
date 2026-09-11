package com.example.multilang

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.multilang.i18n.AppLanguage
import com.example.multilang.i18n.LanguageStorage
import com.example.multilang.i18n.LocalStrings
import com.example.multilang.i18n.stringsFor
import com.example.multilang.i18n.systemLanguageCode
import com.example.multilang.ui.LoginScreen

@Composable
fun App() {
    // Single source of truth for the current language. It seeds itself from
    // whatever the user picked last time, falling back to the OS language,
    // and finally to AppLanguage.default.
    var language by remember {
        mutableStateOf(AppLanguage.fromCode(LanguageStorage.load() ?: systemLanguageCode()))
    }

    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()) {
        Surface(modifier = Modifier, color = MaterialTheme.colorScheme.background) {
            // Every composable below this line can read LocalStrings.current
            // and will recompose automatically when `language` changes.
            CompositionLocalProvider(LocalStrings provides stringsFor(language)) {
                LoginScreen(
                    currentLanguage = language,
                    onLanguageSelected = { selected ->
                        language = selected
                        LanguageStorage.save(selected.code)
                    },
                )
            }
        }
    }
}
