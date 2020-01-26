package xute.markdeditor.utilities

import xute.markdeditor.MarkDEditor
import xute.markdeditor.Styles.TextComponentStyle
import xute.markdeditor.components.TextComponentItem
import xute.markdeditor.datatype.DraftDataItemModel
import xute.markdeditor.models.DraftModel

class RenderingUtils {
    private var markDEditor: MarkDEditor? = null
    fun setEditor(markDEditor: MarkDEditor?) {
        this.markDEditor = markDEditor
    }

    fun render(contents: List<DraftDataItemModel>) { //visit each item type
        for (i in contents.indices) {
            val item = contents[i]
            //identify item of data
            if (item.itemType == DraftModel.ITEM_TYPE_TEXT) { //identify mode of text item
                when (item.mode) {
                    TextComponentItem.MODE_PLAIN ->  //includes NORMAL, H1-H5, Blockquote
                        renderPlainData(item)
                    TextComponentItem.MODE_OL ->  //renders orderedList
                        renderOrderedList(item)
                    TextComponentItem.MODE_UL ->  //renders unorderedList
                        renderUnOrderedList(item)
                    else ->  //default goes to normal text
                        renderPlainData(item)
                }
            } else if (item.itemType == DraftModel.ITEM_TYPE_HR) {
                renderHR()
            } else if (item.itemType == DraftModel.ITEM_TYPE_IMAGE) {
                renderImage(item)
            }
        }
    }

    /**
     * Sets mode to plain and insert a a text component.
     *
     * @param item model of text data item
     */
    private fun renderPlainData(item: DraftDataItemModel) {
        markDEditor!!.setCurrentInputMode(TextComponentItem.MODE_PLAIN)
        when (item.style) {
            TextComponentStyle.NORMAL, TextComponentStyle.H1, TextComponentStyle.H2, TextComponentStyle.H3, TextComponentStyle.H4, TextComponentStyle.H5, TextComponentStyle.BLOCKQUOTE -> {
                markDEditor!!.addTextComponent(insertIndex, item.content)
                markDEditor!!.setHeading(item.style)
            }
            else -> {
                markDEditor!!.addTextComponent(insertIndex, item.content)
                markDEditor!!.setHeading(TextComponentStyle.NORMAL)
            }
        }
    }

    /**
     * Sets mode to ordered-list and insert a a text component.
     *
     * @param item model of text data item.
     */
    private fun renderOrderedList(item: DraftDataItemModel) {
        markDEditor!!.setCurrentInputMode(TextComponentItem.MODE_OL)
        markDEditor!!.addTextComponent(insertIndex, item.content)
    }

    /**
     * Sets mode to unordered-list and insert a a text component.
     *
     * @param item model of text data item.
     */
    private fun renderUnOrderedList(item: DraftDataItemModel) {
        markDEditor!!.setCurrentInputMode(TextComponentItem.MODE_UL)
        markDEditor!!.addTextComponent(insertIndex, item.content)
    }

    /**
     * Adds Horizontal line.
     */
    private fun renderHR() {
        markDEditor!!.insertHorizontalDivider(false)
    }

    /**
     * @param item model of image item.
     * Inserts image.
     * Sets caption
     */
    private fun renderImage(item: DraftDataItemModel) {
        markDEditor!!.insertImage(insertIndex, item.downloadUrl, true)
    }

    /**
     * Since childs are going to be arranged in linear fashion, child count can act as insert index.
     *
     * @return insert index.
     */
    private val insertIndex: Int
        private get() = markDEditor!!.childCount
}