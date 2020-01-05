package joetr.com.nohtes_real

import android.app.Application
import joetr.com.data.di.RoomModule
import joetr.com.nohtes_real.di.component.AppComponent
import joetr.com.nohtes_real.di.component.DaggerAppComponent
import joetr.com.nohtes_real.di.module.AppModule


class NohtesApplication : Application() {

    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder().appModule(AppModule(this)).roomModule(RoomModule(this)).build()
        component.inject(this)
    }

}