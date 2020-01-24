package joetr.com.data

import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import joetr.com.data.dao.LabelDao
import joetr.com.data.entities.LabelEntity
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LabelRepository @Inject constructor(private val labelDao: LabelDao) {
    fun getAllLabels() : Maybe<List<LabelEntity>> {
        return labelDao.getAll()
            .subscribeOn(Schedulers.io())
    }

    fun add(labelEntity: LabelEntity) : Maybe<Long> {
        return labelDao.insert(labelEntity)
            .subscribeOn(Schedulers.io())
    }

    fun delete(labelEntity: LabelEntity) : Single<Int> {
        return labelDao.delete(labelEntity)
            .subscribeOn(Schedulers.io())
    }
}