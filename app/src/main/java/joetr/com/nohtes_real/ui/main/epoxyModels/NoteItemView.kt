package joetr.com.nohtes_real.ui.main.epoxyModels

import android.view.LayoutInflater
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonPlugin
import io.noties.markwon.image.ImagesPlugin
import io.noties.markwon.image.data.DataUriSchemeHandler
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
        val x = Markwon.builder(holder.noteItemEditor.context).usePlugin(ImagesPlugin.create())
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configure(registry: MarkwonPlugin.Registry) {
                    registry.require(
                        ImagesPlugin::class.java
                    ) { imagesPlugin -> imagesPlugin.addSchemeHandler(DataUriSchemeHandler.create()) }
                }
            }).build()
        x.setMarkdown(holder.noteItemEditor, noteEntity.markDown)

        holder.noteItemBaseLayout.setOnClickListener {
            actionHandler(MainPageAction.NoteClicked(noteEntity))
        }

        holder.noteItemEditor.setOnClickListener {
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
        holder.noteItemEditor.setOnClickListener(null)
    }
}

class NoteItemViewHolder : KotlinEpoxyHolder() {
    val noteItemEditor by bind<TextView>(R.id.noteItemEditor)
    val noteItemBaseLayout by bind<CardView>(R.id.noteItemBaseLayout)
    val noteItemChipGroup by bind<ChipGroup>(R.id.noteItemChipGroup)
}