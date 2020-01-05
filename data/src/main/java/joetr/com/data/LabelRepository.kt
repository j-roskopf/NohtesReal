package joetr.com.data

import io.reactivex.Maybe
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
}