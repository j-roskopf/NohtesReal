package joetr.com.nohtes_real.android.base

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.disposables.CompositeDisposable
import joetr.com.nohtes_real.NohtesApplication
import joetr.com.nohtes_real.di.DaggerFragmentAware
import joetr.com.nohtes_real.di.component.DaggerFragmentComponent
import joetr.com.nohtes_real.di.component.FragmentComponent
import joetr.com.nohtes_real.di.module.FragmentModule


abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() , DaggerFragmentAware {

    protected val compositeDisposable = CompositeDisposable()

    val app get() = (requireActivity().application as NohtesApplication)
    val component: FragmentComponent by lazy {
        DaggerFragmentComponent.builder().appComponent(NohtesApplication.component).fragmentModule(
            FragmentModule(this)
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