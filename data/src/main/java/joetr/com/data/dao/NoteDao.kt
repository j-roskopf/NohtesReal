package joetr.com.data.dao

import androidx.room.*
import io.reactivex.Maybe
import io.reactivex.Single
import joetr.com.data.entities.NoteEntity

@Dao
interface NoteDao {
    @Query("SELECT * FROM noteentity")
    fun getAll(): Maybe<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(noteEntity: NoteEntity) : Maybe<Long>

    @Delete
    fun delete(noteEntity: NoteEntity) : Single<Int>
}