package joetr.com.nohtes_real.di.component

import dagger.Component
import joetr.com.nohtes_real.MainActivity
import joetr.com.nohtes_real.android.base.BaseActivity
import joetr.com.nohtes_real.di.module.ActivityModule
import joetr.com.nohtes_real.di.scope.ActivityScope
import joetr.com.nohtes_real.di.scope.FragmentScope


@ActivityScope
@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(baseActivity: BaseActivity)
    fun inject(mainActivity: MainActivity)
}