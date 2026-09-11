# MultiLang Login — KMP Localization Demo

A small Kotlin Multiplatform (KMP) app — a login screen — built specifically to
teach one thing well: **how to handle multiple languages (English, German,
French) in Kotlin, shared across Android and Desktop, without per-platform
resource files.**

Run it, read it top to bottom, then try the exercises at the end.

---

## 1. What's actually in this project

```
MultiLangKMP/
├── composeApp/
│   ├── build.gradle.kts                 ← KMP + Compose Multiplatform + Android config
│   └── src/
│       ├── commonMain/kotlin/com/example/multilang/
│       │   ├── App.kt                   ← app root: owns "current language" state
│       │   ├── i18n/
│       │   │   ├── AppLanguage.kt       ← enum: EN / DE / FR
│       │   │   ├── Strings.kt           ← interface: every translatable key
│       │   │   ├── StringsEn.kt         ← English translations
│       │   │   ├── StringsDe.kt         ← German translations
│       │   │   ├── StringsFr.kt         ← French translations
│       │   │   ├── LocalStrings.kt      ← CompositionLocal + stringsFor() lookup
│       │   │   └── Platform.kt          ← expect: system locale + persisted choice
│       │   └── ui/
│       │       ├── LoginScreen.kt       ← the actual login form
│       │       └── LanguageSwitcher.kt  ← the EN/DE/FR chip row
│       ├── androidMain/kotlin/com/example/multilang/
│       │   ├── MainActivity.kt          ← Android entry point
│       │   ├── AppContext.kt            ← holds Context for SharedPreferences
│       │   └── i18n/Platform.android.kt ← actual: Locale + SharedPreferences
│       ├── androidMain/res/values/strings.xml  ← ONLY the OS launcher label
│       └── desktopMain/kotlin/com/example/multilang/
│           ├── Main.kt                  ← Desktop entry point
│           └── i18n/Platform.desktop.kt ← actual: Locale + java.util.prefs
```

Everything under `commonMain` compiles once and runs unchanged on Android and
Desktop (and would run on iOS with no changes at all — see §7).

---

## 2. The core idea: strings are a Kotlin interface, not a resource file

The old Android way:

```
res/values/strings.xml        <string name="login_title">Welcome back</string>
res/values-de/strings.xml     <string name="login_title">Willkommen zurück</string>
res/values-fr/strings.xml     <string name="login_title">Bon retour</string>
```

Problems with this for a KMP app: it's Android-only (iOS needs `.strings`,
Desktop needs something else), the keys are just strings matched at runtime
(typo a key name and you get a crash or a missing-resource fallback, not a
compile error), and nothing forces every locale file to define every key.

**This project's way** — [`Strings.kt`](composeApp/src/commonMain/kotlin/com/example/multilang/i18n/Strings.kt):

```kotlin
interface Strings {
    val appName: String
    val loginTitle: String
    val loginSubtitle: String
    // ...
    val errorEmailRequired: String
    fun loginSuccessMessage(name: String): String   // parameterized text = a function
}
```

Then one `object` per language implements it —
[`StringsEn.kt`](composeApp/src/commonMain/kotlin/com/example/multilang/i18n/StringsEn.kt):

```kotlin
object StringsEn : Strings {
    override val appName = "MultiLang Login"
    override val loginTitle = "Welcome back"
    override val loginSubtitle = "Sign in to continue"
    // ...
    override val errorEmailRequired = "Email is required"
    override fun loginSuccessMessage(name: String) = "Welcome, $name."
}
```

[`StringsDe.kt`](composeApp/src/commonMain/kotlin/com/example/multilang/i18n/StringsDe.kt) and
[`StringsFr.kt`](composeApp/src/commonMain/kotlin/com/example/multilang/i18n/StringsFr.kt) implement the exact same interface in German and French.

**Why this is better for learning "best practice" i18n in Kotlin:**

| | XML resources | `Strings` interface |
|---|---|---|
| Missing translation | Silent fallback or crash at runtime | **Compile error** — `StringsDe` "does not implement Strings" |
| Renaming a key | Grep and hope | Compiler shows every call site that breaks |
| Works on | Android only | Android, Desktop, iOS, Web — anywhere Kotlin runs |
| Parameterized text (`"Welcome, %s"`) | `String.format`, platform-specific | A regular Kotlin function, type-checked |
| Discoverability | Search XML by string name | Autocomplete on `LocalStrings.current.` |

There's exactly **one** XML file left in the project —
[`androidMain/res/values/strings.xml`](composeApp/src/androidMain/res/values/strings.xml) — holding
just `app_name`, the label Android shows on the launcher icon/task switcher
*before* your Kotlin code runs. The OS has to read that natively, so it's the
one piece of text that stays in the old system. Everything else — every word
on the login screen — comes from Kotlin.

---

## 3. Wiring it into Compose: `CompositionLocal`

Passing `strings: Strings` as a parameter through every composable would be
tedious. Instead, [`LocalStrings.kt`](composeApp/src/commonMain/kotlin/com/example/multilang/i18n/LocalStrings.kt)
defines a `CompositionLocal`:

```kotlin
val LocalStrings = staticCompositionLocalOf<Strings> {
    error("No Strings provided — wrap content in CompositionLocalProvider(...)")
}

fun stringsFor(language: AppLanguage): Strings = when (language) {
    AppLanguage.ENGLISH -> StringsEn
    AppLanguage.GERMAN -> StringsDe
    AppLanguage.FRENCH -> StringsFr
}
```

[`App.kt`](composeApp/src/commonMain/kotlin/com/example/multilang/App.kt) provides it once, at the root:

```kotlin
CompositionLocalProvider(LocalStrings provides stringsFor(language)) {
    LoginScreen(currentLanguage = language, onLanguageSelected = { ... })
}
```

Any composable anywhere below that point reads the current language with:

```kotlin
val strings = LocalStrings.current
Text(strings.loginTitle)
```

When `language` changes, Compose recomposes everything reading `LocalStrings`
automatically — that's the entire mechanism behind the live language switch
you saw in the screenshots. No manual "refresh the UI" step.

---

## 4. Remembering the user's choice: `expect`/`actual`

Two things need real platform APIs that don't exist in shared Kotlin:
reading the device's default language, and persisting the user's pick across
restarts. [`Platform.kt`](composeApp/src/commonMain/kotlin/com/example/multilang/i18n/Platform.kt)
declares the *shape* of both, with no implementation:

```kotlin
expect fun systemLanguageCode(): String

expect object LanguageStorage {
    fun save(languageCode: String)
    fun load(): String?
}
```

Each platform supplies the `actual`:

- **Android** — [`Platform.android.kt`](composeApp/src/androidMain/kotlin/com/example/multilang/i18n/Platform.android.kt): `Locale.getDefault().language` + `SharedPreferences`.
- **Desktop** — [`Platform.desktop.kt`](composeApp/src/desktopMain/kotlin/com/example/multilang/i18n/Platform.desktop.kt): `Locale.getDefault().language` + `java.util.prefs.Preferences`.

`App.kt` seeds the initial language once:

```kotlin
var language by remember {
    mutableStateOf(AppLanguage.fromCode(LanguageStorage.load() ?: systemLanguageCode()))
}
```

Order of precedence: **saved choice → OS language → English default.** Every
time the user taps a language chip, `LanguageStorage.save(...)` persists it,
so relaunching the app remembers the pick.

---

## 5. How to add a new translation key

Say you're adding a "Welcome back, {name}" banner and need a new key.

1. Add it to the interface in `Strings.kt`:
   ```kotlin
   val bannerGreeting: String
   ```
2. Build (or just look at the IDE) — `StringsEn`, `StringsDe`, `StringsFr` are
   now flagged: *"Class is not abstract and does not implement member
   `bannerGreeting`."* That's the mechanism doing its job — it's telling you
   exactly which three files need updating.
3. Fill it in in all three:
   ```kotlin
   // StringsEn.kt
   override val bannerGreeting = "Welcome back!"
   // StringsDe.kt
   override val bannerGreeting = "Willkommen zurück!"
   // StringsFr.kt
   override val bannerGreeting = "Bon retour !"
   ```
4. Use it anywhere: `Text(LocalStrings.current.bannerGreeting)`.

For text with a variable in it, use a function instead of a template string
(commonMain has no `String.format`/`%s`):

```kotlin
// Strings.kt
fun itemCount(count: Int): String

// StringsEn.kt
override fun itemCount(count: Int) = "$count items"
// StringsDe.kt
override fun itemCount(count: Int) = "$count Artikel"
```

---

## 6. How to add a whole new language

Adding Spanish, for example:

1. **`AppLanguage.kt`** — add the enum entry:
   ```kotlin
   SPANISH("es", "Español", "🇪🇸"),
   ```
2. **Create `StringsEs.kt`** implementing `Strings`, translating every key.
   (Copy `StringsEn.kt` as a starting template — the compiler will flag any
   key you forget to change... actually it won't flag untranslated-but-copied
   text, only *missing* keys, so this is the one step review still matters for.)
3. **`LocalStrings.kt`** — register it in the lookup:
   ```kotlin
   fun stringsFor(language: AppLanguage): Strings = when (language) {
       AppLanguage.ENGLISH -> StringsEn
       AppLanguage.GERMAN -> StringsDe
       AppLanguage.FRENCH -> StringsFr
       AppLanguage.SPANISH -> StringsEs   // compiler forces this — `when` is exhaustive
   }
   ```

That's it — [`LanguageSwitcher.kt`](composeApp/src/commonMain/kotlin/com/example/multilang/ui/LanguageSwitcher.kt)
loops over `AppLanguage.entries`, so the new chip appears automatically with
no UI code changes.

---

## 7. Running it

**Desktop** (fastest way to see changes):
```bash
./gradlew :composeApp:run
```

**Android** (emulator or device must be running/connected):
```bash
./gradlew :composeApp:installDebug
```
then launch "MultiLang Login" from the app drawer, or:
```bash
adb shell am start -n com.example.multilang/.MainActivity
```

Both targets share 100% of `commonMain`, including all localization code —
that's the whole demonstration in one command.

**Path to iOS:** none of the localization code (`Strings`, `AppLanguage`,
`LocalStrings`, `LoginScreen`) would need to change. You'd add an `iosMain`
source set with an `actual` `Platform.ios.kt` (`NSLocale.currentLocale` +
`NSUserDefaults` instead of `Locale`/`SharedPreferences`) and a thin Xcode
project embedding a `ComposeUIViewController` — standard KMP wizard output.

---

## 8. Things worth noticing while you read the code

- **`stringsFor()`'s `when` has no `else` branch.** That's deliberate — if you
  add a language to the enum and forget to register it here, the compiler
  errors instead of silently falling through to a wrong language.
- **`LocalStrings` has no default value** (`LoginScreen.kt` would crash with a
  clear message if you forgot the `CompositionLocalProvider`), rather than
  quietly defaulting to English. A loud failure while developing beats a
  quiet wrong-language bug a translator reports weeks later.
- **Validation error messages re-run through the current `strings` on every
  recomposition** (see the top of `LoginScreen.kt`). Try it: trigger an email
  error in English, then switch to French *without* fixing the field — the
  error text updates immediately instead of staying stale in English.
- **`Strings` is UI text only.** Things like date/number formatting,
  pluralization rules, or right-to-left layout are different problems with
  different tools (`kotlinx-datetime` for dates, ICU `MessageFormat`-style
  libraries for plurals) — intentionally out of scope here so the core idea
  stays clear.

---

## 9. Suggested exercises

1. Add a fourth language (Spanish or Italian) end-to-end using §6.
2. Add pluralization: `fun itemCount(n: Int): String` that says "1 item" vs
   "3 items" in English and gets the German/French plural rules right too.
3. Break it on purpose — comment out `override val loginTitle` in
   `StringsDe.kt` and read the compiler error. That error message *is* the
   safety net this whole architecture buys you over `strings.xml`.
4. Add the `iosMain` target following §7's outline.
