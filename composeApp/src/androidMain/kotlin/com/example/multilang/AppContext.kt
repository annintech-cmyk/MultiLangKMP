package com.example.multilang

import android.content.Context

/**
 * Holds the application Context so platform `actual` implementations (like
 * LanguageStorage on Android) can reach SharedPreferences without threading
 * a Context through commonMain code. Set once from MainActivity.onCreate.
 */
object AppContext {
    lateinit var instance: Context
        private set

    fun init(context: Context) {
        instance = context.applicationContext
    }
}
