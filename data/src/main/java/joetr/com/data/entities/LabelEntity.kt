package joetr.com.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class LabelEntity(
     @PrimaryKey val label: String,
     var checked : Boolean = false
): Parcelable