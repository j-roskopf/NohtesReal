package xute.markdeditor.utilities

import android.view.View
import xute.markdeditor.MarkDEditor
import xute.markdeditor.Styles.TextComponentStyle
import xute.markdeditor.components.HorizontalDividerComponentItem
import xute.markdeditor.components.ImageComponentItem
import xute.markdeditor.components.TextComponentItem
import xute.markdeditor.datatype.DraftDataItemModel
import xute.markdeditor.models.ComponentTag
import xute.markdeditor.models.DraftModel
import xute.markdeditor.models.TextComponentModel
import java.util.*

class DraftManager {
    /**
     * Traverse through each item and prepares the draft item list.
     * @param markDEditor editor object.
     * @return a list of Draft Item types.
     */
    fun processDraftContent(markDEditor: MarkDEditor): DraftModel {
        val drafts =
            ArrayList<DraftDataItemModel>()
        val childCount = markDEditor.childCount
        var view: View
        var textStyle: Int
        var componentTag: ComponentTag
        for (i in 0 until childCount) {
            view = markDEditor.getChildAt(i)
            when (view) {
                is TextComponentItem -> { //check mode
                    when (view.mode) {
                        TextComponentItem.MODE_PLAIN -> { //check for styles {H1-H5 Blockquote Normal}
                            componentTag = view.getTag() as ComponentTag
                            textStyle =
                                (componentTag.component as TextComponentModel).headingStyle
                            drafts.add(getPlainModel(textStyle, view.content))
                        }
                        TextComponentItem.MODE_UL -> {
                            drafts.add(getUlModel(view.content))
                        }
                        TextComponentItem.MODE_OL -> {
                            drafts.add(getOlModel(view.content))
                        }
                    }
                }
                is HorizontalDividerComponentItem -> {
                    drafts.add(hRModel)
                }
                is ImageComponentItem -> {
                    drafts.add(
                        getImageModel(
                            view.downloadUrl,
                            view.caption
                        )
                    )
                }
            }
        }
        return DraftModel(drafts)
    }

    /**
     * Models Text information.
     * @param textStyle style associated with the text (NORMAL,H1-H5,BLOCKQUOTE)
     * @param content   text content
     * @return          a Generic TextType Object containing information.
     */
    private fun getPlainModel(textStyle: Int, content: String): DraftDataItemModel {
        val textType = DraftDataItemModel()
        textType.itemType = DraftModel.ITEM_TYPE_TEXT
        textType.content = content
        textType.mode = TextComponentItem.MODE_PLAIN
        textType.style = textStyle
        return textType
    }

    /**
     * Models UnOrdered list text information.
     * @param content text content.
     * @return a UL type model object.
     */
    private fun getUlModel(content: String): DraftDataItemModel {
        val textType = DraftDataItemModel()
        textType.itemType = DraftModel.ITEM_TYPE_TEXT
        textType.content = content
        textType.mode = TextComponentItem.MODE_UL
        textType.style = TextComponentStyle.NORMAL
        return textType
    }

    /**
     * Models Ordered list text information.
     * @param content text content.
     * @return a OL type model object.
     */
    private fun getOlModel(content: String): DraftDataItemModel {
        val textType = DraftDataItemModel()
        textType.itemType = DraftModel.ITEM_TYPE_TEXT
        textType.content = content
        textType.mode = TextComponentItem.MODE_OL
        textType.style = TextComponentStyle.NORMAL
        return textType
    }

    /**
     * Models Horizontal rule object.
     * @return a HR type model object.
     */
    private val hRModel: DraftDataItemModel
        get() {
            val hrType = DraftDataItemModel()
            hrType.itemType = DraftModel.ITEM_TYPE_HR
            return hrType
        }

    /**
     * Models Image type object item.
     * @param downloadUrl  url of the image.
     * @param caption      caption of the image(if any)
     * @return             a Image Model object type.
     */
    private fun getImageModel(
        downloadUrl: String,
        caption: String
    ): DraftDataItemModel {
        val imageType = DraftDataItemModel()
        imageType.itemType = DraftModel.ITEM_TYPE_IMAGE
        imageType.caption = caption
        imageType.downloadUrl = downloadUrl
        return imageType
    }
}