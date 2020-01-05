package joetr.com.nohtes_real.di

import androidx.annotation.NonNull
import joetr.com.nohtes_real.di.component.ActivityComponent

interface DaggerActivityAware {
    fun injectSelf(@NonNull component: ActivityComponent)
}
