package joetr.com.nohtes_real.ui.main.epoxyModels

import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.github.irshulx.Editor
import joetr.com.data.entities.NoteEntity
import joetr.com.nohtes_real.R
import joetr.com.nohtes_real.android.extensions.KotlinEpoxyHolder
import joetr.com.nohtes_real.ui.main.MainActionHandler
import joetr.com.nohtes_real.ui.main.MainPageAction

@EpoxyModelClass(layout = R.layout.note_item)
abstract class NoteItemView : EpoxyModelWithHolder<NoteItemViewHolder>() {

    @EpoxyAttribute
    lateinit var noteEntity: NoteEntity

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var actionHandler: MainActionHandler

    override fun bind(holder: NoteItemViewHolder) {
        holder.noteItemEditor.clearAllContents()
        holder.noteItemEditor.render(noteEntity.content)
        holder.noteItemBaseLayout.setOnClickListener {
            actionHandler(MainPageAction.NoteClicked(noteEntity))
        }
    }

    override fun unbind(holder: NoteItemViewHolder) {
        super.unbind(holder)
        holder.noteItemBaseLayout.setOnClickListener(null)
    }
}

class NoteItemViewHolder : KotlinEpoxyHolder() {
    val noteItemEditor by bind<Editor>(R.id.noteItemEditor)
    val noteItemBaseLayout by bind<ConstraintLayout>(R.id.noteItemBaseLayout)
}