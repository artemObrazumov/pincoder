package com.artemobrazumov.pincoder.shared_preferences

import android.content.Context
import com.google.gson.Gson

class SharedPreferencesRepository(
    private val context: Context,
    private val gson: Gson
) {
    var isCorrectPin: Boolean?
        get() = context.sharedPreferences.getBoolean(IS_CORRECT_PIN_KEY, false)
        set(isCorrectPin) {
            context.sharedPreferences.edit()
                .putBoolean(IS_CORRECT_PIN_KEY, isCorrectPin ?: false)
                .apply()
        }

    var pinCode: String?
        get() = context.sharedPreferences.getString(PIN_KEY, null)
        set(pinCode) {
            context.sharedPreferences.edit().putString(PIN_KEY, pinCode).apply()
        }

    @Suppress("UNCHECKED_CAST")
    var packagesId: List<String>
        get() = gson.fromJson(
            context.sharedPreferences.getString(
                PACKAGE_ID_LIST_KEY, gson.toJson(emptyList<String>())
            ),
            List::class.java
        ) as List<String>
        set(list) {
            context.sharedPreferences.edit().putString(PACKAGE_ID_LIST_KEY, gson.toJson(list)).apply()
        }

    @Suppress("UNCHECKED_CAST")
    var securedAppCache: List<String>
        get() = gson.fromJson(
            context.sharedPreferences.getString(
                SECURED_APP_CACHE_KEY, gson.toJson(emptyList<String>())
            ),
            List::class.java
        ) as List<String>
        set(list) {
            context.sharedPreferences.edit().putString(SECURED_APP_CACHE_KEY, gson.toJson(list)).apply()
        }

    private val Context.sharedPreferences
        get() = getSharedPreferences(SECURE_PIN_ON_APP_STORAGE, Context.MODE_PRIVATE)

    companion object {
        private const val PACKAGE_ID_LIST_KEY = "PACKAGE_ID_LIST"
        private const val PIN_KEY = "PIN"
        private const val IS_CORRECT_PIN_KEY = "IS_CORRECT_PIN"
        private const val SECURED_APP_CACHE_KEY = "SECURED_APP_CACHE"
        private const val SECURE_PIN_ON_APP_STORAGE = "secure_pin_storage"
    }
}