package ru.forblitz.statistics.service

import android.content.SharedPreferences

class PreferencesService(private val sharedPreferences: SharedPreferences) {

    var region: String?
        get() = sharedPreferences.getString("region", "ru")
        set(value) = sharedPreferences.edit().putString("region", value).apply()

    var locale: String?
        get() = sharedPreferences.getString("locale", "notSpecified")
        set(value) = sharedPreferences.edit().putString("locale", value).apply()

    var isPrivacyPolicyAccepted: Boolean
        get() = sharedPreferences.getBoolean("isPrivacyPolicyAccepted", false)
        set(value) = sharedPreferences.edit().putBoolean("isPrivacyPolicyAccepted", value).apply()

    fun get(tag: String): Boolean {
        return sharedPreferences.getBoolean(tag, false)
    }

    fun set(tag: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(tag, value).apply()
    }

}