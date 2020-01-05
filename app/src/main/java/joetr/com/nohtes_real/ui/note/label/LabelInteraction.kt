package joetr.com.nohtes_real.ui.note.label

import joetr.com.data.entities.LabelEntity

interface LabelInteraction {
    fun labelClicked(label: LabelEntity)
}