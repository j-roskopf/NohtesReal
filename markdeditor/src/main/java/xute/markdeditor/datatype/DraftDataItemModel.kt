package xute.markdeditor.datatype

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class DraftDataItemModel(
    var itemType: Int = 0,
    var mode: Int = 0,
    var style: Int = 0,
    var content: String? = null,
    var downloadUrl: String? = null
) : Parcelable