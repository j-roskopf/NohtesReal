package joetr.com.nohtes_real.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import joetr.com.nohtes_real.NohtesApplication
import joetr.com.nohtes_real.di.scope.ApplicationContext


@Module
class AppModule(private val application: NohtesApplication) {

    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return application
    }

    @Provides
    fun provideApplication(): Application {
        return application
    }
}