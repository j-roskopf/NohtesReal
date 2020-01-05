package joetr.com.nohtes_real.di

import androidx.annotation.NonNull
import joetr.com.nohtes_real.di.component.FragmentComponent

interface DaggerFragmentAware {
    fun injectSelf(@NonNull component: FragmentComponent)
}
