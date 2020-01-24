package joetr.com.nohtes_real.ui.note.label.dataModels

import joetr.com.data.entities.LabelEntity

sealed class LabelItem(open val labelEntity: LabelEntity) {
    data class UserEnteredLabelItem (override val labelEntity: LabelEntity) : LabelItem(labelEntity)
    data class AddNewLabelItem (override val labelEntity: LabelEntity) : LabelItem(labelEntity)
}