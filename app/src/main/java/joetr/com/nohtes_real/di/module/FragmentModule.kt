package joetr.com.nohtes_real.di.module

import android.content.Context
import androidx.fragment.app.Fragment
import dagger.Module
import dagger.Provides
import joetr.com.nohtes_real.di.scope.ActivityContext

@Module
class FragmentModule constructor(private val fragment: Fragment) {

    @Provides
    internal fun provideFragment(): Fragment = fragment

    @Provides
    @ActivityContext
    internal fun provideFragmentContext(): Context = fragment.context!!
}