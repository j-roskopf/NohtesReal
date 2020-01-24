package joetr.com.nohtes_real.ui.note.label

import com.airbnb.epoxy.Typed2EpoxyController
import joetr.com.nohtes_real.android.extensions.exhaustive
import joetr.com.nohtes_real.ui.note.label.dataModels.LabelItem
import joetr.com.nohtes_real.ui.note.label.epoxyModels.addNewLabelItem
import joetr.com.nohtes_real.ui.note.label.epoxyModels.labelItemView

private const val ADD_NEW_LABEL_ID = "add_new_label_id"

class LabelController : Typed2EpoxyController<List<LabelItem>, LabelActionHandler>() {
    override fun buildModels(data: List<LabelItem>, actionHandler: LabelActionHandler) {
        data.forEach { labelItem ->
            when(labelItem) {
                is LabelItem.UserEnteredLabelItem -> labelItemView {
                    id(labelItem.labelEntity.label)
                    labelEntity(labelItem.labelEntity)
                    actionHandler(actionHandler)
                }
                is LabelItem.AddNewLabelItem -> addNewLabelItem {
                    id(ADD_NEW_LABEL_ID)
                    actionHandler(actionHandler)
                }
            }.exhaustive
        }
    }
}