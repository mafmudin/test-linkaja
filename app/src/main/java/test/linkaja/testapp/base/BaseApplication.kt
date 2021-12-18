package test.linkaja.testapp.base

import android.app.Application
import android.content.ContextWrapper
import com.pixplicity.easyprefs.library.BuildConfig
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BaseApplication: Application() {
    /**
     * init the application here
     *
     * setup easy prefs to help use shared preferences
     *
     * setup timber on debug mode only
     */
    override fun onCreate() {
        super.onCreate()
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}