package com.example.multilang.i18n

object StringsDe : Strings {
    override val appName = "MultiLang Login"

    override val loginTitle = "Willkommen zurück"
    override val loginSubtitle = "Melde dich an, um fortzufahren"
    override val emailLabel = "E-Mail"
    override val emailPlaceholder = "du@beispiel.de"
    override val passwordLabel = "Passwort"
    override val passwordPlaceholder = "Passwort eingeben"
    override val showPassword = "Passwort anzeigen"
    override val hidePassword = "Passwort verbergen"
    override val rememberMe = "Angemeldet bleiben"
    override val forgotPassword = "Passwort vergessen?"
    override val loginButton = "Anmelden"
    override val noAccount = "Noch kein Konto?"
    override val signUp = "Registrieren"
    override val languagePickerLabel = "Sprache"

    override val errorEmailRequired = "E-Mail wird benötigt"
    override val errorEmailInvalid = "Bitte eine gültige E-Mail-Adresse eingeben"
    override val errorPasswordRequired = "Passwort wird benötigt"
    override val errorPasswordTooShort = "Das Passwort muss mindestens 6 Zeichen lang sein"

    override val loginSuccessTitle = "Du bist angemeldet!"
    override fun loginSuccessMessage(name: String) = "Willkommen, $name."
    override val logOut = "Abmelden"
}
