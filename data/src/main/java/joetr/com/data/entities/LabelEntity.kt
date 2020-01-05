package joetr.com.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LabelEntity(
     @PrimaryKey val label: String,
     var checked : Boolean = false
)