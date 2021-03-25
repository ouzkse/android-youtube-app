package android.app.ouzkse.youtube.util

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

object AppThemeHelper {

    private const val NIGHT_MODE = "night_mode"

    private val MODE_NIGHT_DEFAULT = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    } else {
        AppCompatDelegate.MODE_NIGHT_NO
    }

    fun getSelectedThemeMode(context: Context): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getInt(NIGHT_MODE, MODE_NIGHT_DEFAULT)
    }

    fun setTheme(themeMode: Int) = AppCompatDelegate.setDefaultNightMode(themeMode)

    fun saveTheme(mode: Int, context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putInt(NIGHT_MODE, mode).apply()
        setTheme(mode)
    }
}