package android.app.ouzkse.youtube

import android.app.Application
import android.app.ouzkse.youtube.util.AppThemeHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setTheme()
    }

    private fun setTheme() {
        val mode = AppThemeHelper.getSelectedThemeMode(this)
        AppThemeHelper.setTheme(mode)
    }
}