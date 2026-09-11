package com.example.multilang.i18n

object StringsEn : Strings {
    override val appName = "MultiLang Login"

    override val loginTitle = "Welcome back"
    override val loginSubtitle = "Sign in to continue"
    override val emailLabel = "Email"
    override val emailPlaceholder = "you@example.com"
    override val passwordLabel = "Password"
    override val passwordPlaceholder = "Enter your password"
    override val showPassword = "Show password"
    override val hidePassword = "Hide password"
    override val rememberMe = "Remember me"
    override val forgotPassword = "Forgot password?"
    override val loginButton = "Log in"
    override val noAccount = "Don't have an account?"
    override val signUp = "Sign up"
    override val languagePickerLabel = "Language"

    override val errorEmailRequired = "Email is required"
    override val errorEmailInvalid = "Enter a valid email address"
    override val errorPasswordRequired = "Password is required"
    override val errorPasswordTooShort = "Password must be at least 6 characters"

    override val loginSuccessTitle = "You're logged in!"
    override fun loginSuccessMessage(name: String) = "Welcome, $name."
    override val logOut = "Log out"
}
