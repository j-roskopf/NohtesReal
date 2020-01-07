package joetr.com.nohtes_real.ui.main

import com.airbnb.epoxy.Typed2EpoxyController
import joetr.com.data.entities.NoteEntity
import joetr.com.nohtes_real.ui.main.epoxyModels.noteItemView

class MainController : Typed2EpoxyController<List<NoteEntity>, MainActionHandler>() {

    override fun buildModels(data: List<NoteEntity>, actionHandler: MainActionHandler) {
        data.forEach {
            noteItemView {
                id(it.id)
                noteEntity(it)
                actionHandler(actionHandler)
            }
        }
    }

}