package joetr.com.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)