package com.example.multilang.i18n

/**
 * Every piece of user-facing text in the app, in one place.
 *
 * This is the "master list" of translation keys. Because it's a regular Kotlin
 * interface (not a loosely-typed map), the compiler enforces two things that
 * loose key/value stores (like plain .properties or .json files) can't:
 *   1. Every StringsXx implementation must provide every key — no missing
 *      translations that only blow up at runtime.
 *   2. Renaming or deleting a key here shows every call site that breaks.
 *
 * HOW TO ADD A NEW KEY:
 *   1. Add a `val` for it below (with a doc comment describing where it's used).
 *   2. Kotlin will immediately flag StringsEn, StringsDe and StringsFr as
 *      "not implementing Strings" — that's the reminder to fill it in.
 *   3. Provide the English, German and French text in those three files.
 *   4. Use it in the UI via `LocalStrings.current.yourNewKey`.
 */
interface Strings {
    val appName: String

    // Login screen
    val loginTitle: String
    val loginSubtitle: String
    val emailLabel: String
    val emailPlaceholder: String
    val passwordLabel: String
    val passwordPlaceholder: String
    val showPassword: String
    val hidePassword: String
    val rememberMe: String
    val forgotPassword: String
    val loginButton: String
    val noAccount: String
    val signUp: String
    val languagePickerLabel: String

    // Validation
    val errorEmailRequired: String
    val errorEmailInvalid: String
    val errorPasswordRequired: String
    val errorPasswordTooShort: String

    // Post-login
    val loginSuccessTitle: String

    /**
     * A parameterized string. `String.format`/`%s` isn't available in
     * commonMain, so parameterized text is modeled as a function instead of
     * a template — plain, type-checked string concatenation.
     */
    fun loginSuccessMessage(name: String): String
    val logOut: String
}
