package joetr.com.nohtes_real.android.extensions

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Fragment.setToolbarTitle(title: String) {
    requireActivity().actionBar?.let {
        it.title = title
    }
}

fun Fragment.setToolbarTitle(@StringRes title: Int) {
    setToolbarTitle(getString(title))
}