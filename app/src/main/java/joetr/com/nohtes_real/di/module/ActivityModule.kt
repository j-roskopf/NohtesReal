package joetr.com.nohtes_real.di.module

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import joetr.com.nohtes_real.di.scope.ActivityContext


@Module
class ActivityModule(private val activity: Activity) {

    @Provides
    @ActivityContext
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

}