package com.example.multilang.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.multilang.i18n.AppLanguage

/**
 * A row of chips — one per [AppLanguage] entry. Add a language to the enum
 * and it shows up here automatically, no changes needed in this file.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSwitcher(
    selected: AppLanguage,
    onSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AppLanguage.entries.forEach { language ->
            FilterChip(
                selected = language == selected,
                onClick = { onSelected(language) },
                label = { Text("${language.flag} ${language.nativeName}") },
                modifier = Modifier.padding(vertical = 2.dp),
            )
        }
    }
}
