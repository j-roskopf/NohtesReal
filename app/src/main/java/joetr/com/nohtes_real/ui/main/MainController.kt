package joetr.com.nohtes_real.ui.main

import com.airbnb.epoxy.Typed3EpoxyController
import io.noties.markwon.Markwon
import joetr.com.data.entities.NoteEntity
import joetr.com.nohtes_real.ui.main.epoxyModels.noteItemView

class MainController : Typed3EpoxyController<List<NoteEntity>, MainActionHandler, Markwon>() {
    override fun buildModels(data: List<NoteEntity>, actionHandler: MainActionHandler, markwon: Markwon) {
        data.forEach {
            noteItemView {
                id(it.id)
                noteEntity(it)
                actionHandler(actionHandler)
                markwon(markwon)
            }
        }
    }

}