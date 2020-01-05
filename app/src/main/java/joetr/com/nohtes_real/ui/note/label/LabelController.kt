package joetr.com.nohtes_real.ui.note.label

import com.airbnb.epoxy.Typed2EpoxyController
import joetr.com.data.entities.LabelEntity
import joetr.com.nohtes_real.ui.note.label.epoxyModels.labelItemView

class LabelController : Typed2EpoxyController<List<LabelEntity>, LabelActionHandler>() {
    override fun buildModels(data: List<LabelEntity>, actionHandler: LabelActionHandler) {
        data.forEach { entity ->
            labelItemView {
                id(entity.label)
                labelEntity(entity)
                actionHandler(actionHandler)
            }
        }
    }

}