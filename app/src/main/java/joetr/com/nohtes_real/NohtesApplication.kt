package joetr.com.nohtes_real

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import joetr.com.data.di.RoomModule
import joetr.com.nohtes_real.di.component.AppComponent
import joetr.com.nohtes_real.di.component.DaggerAppComponent
import joetr.com.nohtes_real.di.module.AppModule

const val NIGHT_MODE_KEY = "night_mode"

class NohtesApplication : Application() {

    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder().appModule(AppModule(this)).roomModule(RoomModule(this)).build()
        component.inject(this)

        // set night mode
        val sharedPreferences = getSharedPreferences(
            getString(
                R.string.app_name
            ),
            Context.MODE_PRIVATE
        )

        if(sharedPreferences.getBoolean(NIGHT_MODE_KEY, true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

    }

}