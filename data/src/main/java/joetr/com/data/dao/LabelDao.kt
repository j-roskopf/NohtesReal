package joetr.com.data.dao

import androidx.room.*
import io.reactivex.Maybe
import io.reactivex.Single
import joetr.com.data.entities.LabelEntity

@Dao
interface LabelDao {
    @Query("SELECT * FROM labelentity")
    fun getAll(): Maybe<List<LabelEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(labelEntity: LabelEntity) : Maybe<Long>

    @Insert
    fun insertAllLabels(data: List<LabelEntity>): List<Long>

    @Delete
    fun delete(labelEntity: LabelEntity) : Single<Int>

    @Update
    fun update(labelEntity: LabelEntity) : Single<Int>
}