package com.example.multilang.i18n

object StringsFr : Strings {
    override val appName = "MultiLang Login"

    override val loginTitle = "Bon retour"
    override val loginSubtitle = "Connectez-vous pour continuer"
    override val emailLabel = "E-mail"
    override val emailPlaceholder = "vous@exemple.com"
    override val passwordLabel = "Mot de passe"
    override val passwordPlaceholder = "Entrez votre mot de passe"
    override val showPassword = "Afficher le mot de passe"
    override val hidePassword = "Masquer le mot de passe"
    override val rememberMe = "Se souvenir de moi"
    override val forgotPassword = "Mot de passe oublié ?"
    override val loginButton = "Se connecter"
    override val noAccount = "Pas encore de compte ?"
    override val signUp = "S'inscrire"
    override val languagePickerLabel = "Langue"

    override val errorEmailRequired = "L'e-mail est requis"
    override val errorEmailInvalid = "Saisissez une adresse e-mail valide"
    override val errorPasswordRequired = "Le mot de passe est requis"
    override val errorPasswordTooShort = "Le mot de passe doit contenir au moins 6 caractères"

    override val loginSuccessTitle = "Vous êtes connecté !"
    override fun loginSuccessMessage(name: String) = "Bienvenue, $name."
    override val logOut = "Se déconnecter"
}
