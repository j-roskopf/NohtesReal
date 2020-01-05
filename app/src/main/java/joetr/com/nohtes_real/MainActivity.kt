package joetr.com.nohtes_real

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import joetr.com.data.AppDatabase
import joetr.com.nohtes_real.android.base.BaseActivity
import joetr.com.nohtes_real.di.component.ActivityComponent
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.toolbar))

        // trigger so pre population happens
        compositeDisposable += AppDatabase.getInstance(this).labelDao().getAll().subscribeOn(
            Schedulers.io()).subscribe()

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        findViewById<Toolbar>(R.id.toolbar)
            .setupWithNavController(navController, appBarConfiguration)
    }

    override fun injectSelf(component: ActivityComponent) = component.inject(this)
}
