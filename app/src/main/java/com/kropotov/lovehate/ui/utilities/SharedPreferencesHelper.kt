package com.kropotov.lovehate.ui.utilities

import android.content.Context
import android.content.SharedPreferences
import com.kropotov.lovehate.data.AppTheme
import dagger.Reusable
import javax.inject.Inject

// TODO: Migrate to DataStore
@Reusable
class SharedPreferencesHelper @Inject constructor(
    private val applicationContext: Context
) {

    private val prefs: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
    }

    fun isUserAuthenticated() = !prefs.getString(TOKEN_KEY, null).isNullOrEmpty()

    fun saveToken(token: String) {
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken() = prefs.getString(TOKEN_KEY, "").orEmpty()

    fun clearToken() {
        prefs.edit().remove(TOKEN_KEY).apply()
    }

    fun getNotificationsEnabled() = prefs.getBoolean(ENABLE_NOTIFICATIONS_KEY, false)

    fun saveNotificationEnabled(value: Boolean)
        = prefs.edit().putBoolean(ENABLE_NOTIFICATIONS_KEY, value).apply()

    fun getPreferredTheme(): AppTheme {
        val ordinal = prefs.getInt(APP_THEME_ORDINAL_KEY, 0)
        return AppTheme.entries[ordinal]
    }

    fun savePreferredTheme(theme: AppTheme) {
        prefs.edit().apply {
            putInt(APP_THEME_ORDINAL_KEY, theme.ordinal).apply()
        }
    }

    companion object {
        const val SHARED_PREFERENCES_KEY = "settings"

        const val TOKEN_KEY = "token"
        private const val ENABLE_NOTIFICATIONS_KEY = "enable_notifications"
        private const val APP_THEME_ORDINAL_KEY = "app_theme"
    }
}
