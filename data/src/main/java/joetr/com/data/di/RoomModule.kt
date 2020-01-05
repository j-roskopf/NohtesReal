package joetr.com.data.di

import android.app.Application
import dagger.Module
import dagger.Provides
import joetr.com.data.AppDatabase
import joetr.com.data.NoteRepository
import joetr.com.data.dao.LabelDao
import joetr.com.data.dao.NoteDao
import javax.inject.Singleton


@Module
class RoomModule(application: Application) {

    private val appDatabase: AppDatabase by lazy {
        AppDatabase.getInstance(application)
    }

    @Singleton
    @Provides
    fun providesRoomDatabase(): AppDatabase {
        return appDatabase
    }

    @Singleton
    @Provides
    fun provideNoteDao(appDatabase: AppDatabase): NoteDao {
        return appDatabase.noteDao()
    }

    @Singleton
    @Provides
    fun provideLabelDao(appDatabase: AppDatabase): LabelDao {
        return appDatabase.labelDao()
    }

    @Singleton
    @Provides
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository {
        return NoteRepository(noteDao)
    }
}