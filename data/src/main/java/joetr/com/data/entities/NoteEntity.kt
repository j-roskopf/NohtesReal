package joetr.com.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import xute.markdeditor.datatype.DraftDataItemModel

@Entity
@Parcelize
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val draftContent: List<DraftDataItemModel>,
    val markDown: String,
    val timestamp: Long = System.currentTimeMillis(),
    val labels: List<LabelEntity>?
) : Parcelable