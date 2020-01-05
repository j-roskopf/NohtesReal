package joetr.com.nohtes_real.di.component

import android.content.Context
import dagger.Component
import joetr.com.nohtes_real.android.base.BaseFragment
import joetr.com.nohtes_real.di.module.FragmentModule
import joetr.com.nohtes_real.di.scope.ActivityContext
import joetr.com.nohtes_real.di.scope.ActivityScope
import joetr.com.nohtes_real.di.scope.FragmentScope
import joetr.com.nohtes_real.ui.main.MainFragment
import joetr.com.nohtes_real.ui.note.AddNoteFragment
import joetr.com.nohtes_real.ui.note.label.LabelBottomSheet

@ActivityScope
@FragmentScope
@Component(dependencies = [AppComponent::class],
    modules = [FragmentModule::class]
)
interface FragmentComponent {

    @ActivityContext
    fun getContext(): Context

    fun inject(mainFragment: MainFragment)
    fun inject(baseFragment: BaseFragment)
    fun inject(addToNoteFragment: AddNoteFragment)
    fun inject(labelBottomSheet: LabelBottomSheet)
}