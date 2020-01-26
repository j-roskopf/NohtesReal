package joetr.com.nohtes_real.android.base

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import joetr.com.nohtes_real.NohtesApplication
import joetr.com.nohtes_real.R
import joetr.com.nohtes_real.di.DaggerFragmentAware
import joetr.com.nohtes_real.di.component.DaggerFragmentComponent
import joetr.com.nohtes_real.di.component.FragmentComponent
import joetr.com.nohtes_real.di.module.FragmentModule

abstract class BaseFragment : Fragment(), DaggerFragmentAware {

    protected val compositeDisposable = CompositeDisposable()

    protected lateinit var sharedPreferences: SharedPreferences

    val app get() = (requireActivity().application as NohtesApplication)
    val component: FragmentComponent by lazy {
        DaggerFragmentComponent.builder().appComponent(NohtesApplication.component).fragmentModule(
            FragmentModule(this)
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectSelf(component)

        sharedPreferences = requireActivity().getSharedPreferences(
            getString(
                R.string.app_name
            ),
            MODE_PRIVATE
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}