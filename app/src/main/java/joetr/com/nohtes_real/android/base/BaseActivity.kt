package joetr.com.nohtes_real.android.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import io.reactivex.disposables.CompositeDisposable
import joetr.com.nohtes_real.NohtesApplication
import joetr.com.nohtes_real.di.DaggerActivityAware
import joetr.com.nohtes_real.di.component.ActivityComponent
import joetr.com.nohtes_real.di.component.DaggerActivityComponent
import joetr.com.nohtes_real.di.module.ActivityModule

abstract class BaseActivity : AppCompatActivity(), DaggerActivityAware {

    val compositeDisposable = CompositeDisposable()

    val app get() = (application as NohtesApplication)
    val component: ActivityComponent by lazy {
        DaggerActivityComponent.builder().appComponent(NohtesApplication.component).activityModule(
            ActivityModule(this)
        ).build()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectSelf(component)
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}