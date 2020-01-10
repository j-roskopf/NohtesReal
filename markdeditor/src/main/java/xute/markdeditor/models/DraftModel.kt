package xute.markdeditor.models

import xute.markdeditor.datatype.DraftDataItemModel
import java.util.*

data class DraftModel(var items: List<DraftDataItemModel>?) {
    var draftTitle: String? = null
    var draftId: Long = 0

    companion object {
        const val ITEM_TYPE_TEXT = 0
        const val ITEM_TYPE_IMAGE = 1
        const val ITEM_TYPE_HR = 2
    }
}