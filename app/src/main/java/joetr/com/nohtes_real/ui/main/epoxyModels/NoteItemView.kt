package joetr.com.nohtes_real.ui.main.epoxyModels

import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.github.irshulx.Editor
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import joetr.com.data.entities.NoteEntity
import joetr.com.nohtes_real.R
import joetr.com.nohtes_real.android.extensions.KotlinEpoxyHolder
import joetr.com.nohtes_real.ui.main.MainActionHandler
import joetr.com.nohtes_real.ui.main.MainPageAction
import joetr.com.nohtes_real.ui.note.label.LabelBottomSheet

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

        addChips(holder)
    }

    private fun addChips(holder: NoteItemViewHolder) {
        holder.noteItemChipGroup.removeAllViews()

        noteEntity.labels?.forEach {
            val chip = LayoutInflater.from(holder.noteItemChipGroup.context).inflate(
                R.layout.add_note_chip,
                holder.noteItemChipGroup,
                false
            ) as Chip

            chip.text = it.label
            chip.setOnClickListener {
                actionHandler(MainPageAction.NoteClicked(noteEntity))
            }
            holder.noteItemChipGroup.addView(chip)
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
    val noteItemChipGroup by bind<ChipGroup>(R.id.noteItemChipGroup)
}