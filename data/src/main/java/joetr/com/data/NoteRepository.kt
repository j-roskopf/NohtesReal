package joetr.com.data

import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import joetr.com.data.dao.NoteDao
import joetr.com.data.entities.NoteEntity
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NoteRepository @Inject constructor(private val noteDao: NoteDao) {

    fun insertNote(noteEntity: NoteEntity): Maybe<Long> {
        return noteDao.insert(noteEntity)
            .subscribeOn(Schedulers.io())
    }

}