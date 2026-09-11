package com.example.multilang.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.multilang.i18n.AppLanguage
import com.example.multilang.i18n.LocalStrings
import com.example.multilang.i18n.Strings

@Composable
fun LoginScreen(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
) {
    val strings = LocalStrings.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var loggedInAs by remember { mutableStateOf<String?>(null) }

    // Re-validate already-shown errors when the language changes, so a
    // switch from English to French updates error text still on screen
    // instead of leaving it stale until the next keystroke.
    if (emailError != null) {
        emailError = validateEmail(email, strings)
    }
    if (passwordError != null) {
        passwordError = validatePassword(password, strings)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(24.dp))

        Text(strings.appName, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(24.dp))

        Text(strings.languagePickerLabel, style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(8.dp))
        LanguageSwitcher(
            selected = currentLanguage,
            onSelected = onLanguageSelected,
        )

        Spacer(Modifier.height(32.dp))

        val formModifier = Modifier.widthIn(max = 400.dp).fillMaxWidth()

        if (loggedInAs == null) {
            Column(modifier = formModifier, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(strings.loginTitle, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(4.dp))
                Text(strings.loginSubtitle, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(24.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null
                    },
                    label = { Text(strings.emailLabel) },
                    placeholder = { Text(strings.emailPlaceholder) },
                    isError = emailError != null,
                    supportingText = { emailError?.let { Text(it) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null
                    },
                    label = { Text(strings.passwordLabel) },
                    placeholder = { Text(strings.passwordPlaceholder) },
                    isError = passwordError != null,
                    supportingText = { passwordError?.let { Text(it) } },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        TextButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(if (passwordVisible) strings.hidePassword else strings.showPassword)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
                        Text(strings.rememberMe, style = MaterialTheme.typography.bodyMedium)
                    }
                    TextButton(onClick = { /* demo only */ }) {
                        Text(strings.forgotPassword)
                    }
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        val eError = validateEmail(email, strings)
                        val pError = validatePassword(password, strings)
                        emailError = eError
                        passwordError = pError
                        if (eError == null && pError == null) {
                            loggedInAs = email.substringBefore("@")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(strings.loginButton)
                }

                Spacer(Modifier.height(16.dp))

                Row {
                    Text(strings.noAccount, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.width(4.dp))
                    TextButton(onClick = { /* demo only */ }, contentPadding = PaddingValues(0.dp)) {
                        Text(strings.signUp)
                    }
                }
            }
        } else {
            Column(modifier = formModifier, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(strings.loginSuccessTitle, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
                Text(strings.loginSuccessMessage(loggedInAs!!), style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(24.dp))
                Button(onClick = {
                    loggedInAs = null
                    email = ""
                    password = ""
                }) {
                    Text(strings.logOut)
                }
            }
        }
    }
}

private fun validateEmail(email: String, strings: Strings): String? = when {
    email.isBlank() -> strings.errorEmailRequired
    !email.contains("@") || !email.substringAfter("@").contains(".") -> strings.errorEmailInvalid
    else -> null
}

private fun validatePassword(password: String, strings: Strings): String? = when {
    password.isBlank() -> strings.errorPasswordRequired
    password.length < 6 -> strings.errorPasswordTooShort
    else -> null
}
