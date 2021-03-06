package joetr.com.data

import io.reactivex.Maybe
import io.reactivex.Single
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

    fun getAll(): Maybe<List<NoteEntity>> {
        return noteDao.getAll()
            .subscribeOn(Schedulers.io())
    }

    fun delete(noteEntity: NoteEntity): Single<Int> {
        return noteDao.delete(noteEntity)
            .subscribeOn(Schedulers.io())
    }

}