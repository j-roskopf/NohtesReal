package joetr.com.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val labels: List<LabelEntity>?
) : Parcelable