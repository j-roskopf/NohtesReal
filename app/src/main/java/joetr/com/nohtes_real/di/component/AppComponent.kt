package joetr.com.nohtes_real.di.component

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import joetr.com.data.AppDatabase
import joetr.com.data.di.RoomModule
import joetr.com.nohtes_real.NohtesApplication
import joetr.com.nohtes_real.di.module.AppModule
import joetr.com.nohtes_real.di.viewModel.ViewModelModule
import joetr.com.nohtes_real.ui.note.AddNoteViewModel
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, RoomModule::class, ViewModelModule::class])
interface AppComponent {
    fun addToNoteViewModel(): AddNoteViewModel
    fun appDatabase(): AppDatabase
    fun viewModelFactory(): ViewModelProvider.Factory
    fun inject(demoApplication: NohtesApplication?)
}