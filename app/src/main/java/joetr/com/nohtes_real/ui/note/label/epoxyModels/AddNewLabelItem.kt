package joetr.com.nohtes_real.ui.note.label.epoxyModels

import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import joetr.com.nohtes_real.R
import joetr.com.nohtes_real.android.extensions.KotlinEpoxyHolder
import joetr.com.nohtes_real.ui.note.label.LabelAction
import joetr.com.nohtes_real.ui.note.label.LabelActionHandler

@EpoxyModelClass(layout = R.layout.add_new_label_item)
abstract class AddNewLabelItem : EpoxyModelWithHolder<AddNewLabelItemViewHolder>() {

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var actionHandler: LabelActionHandler

    override fun bind(holder: AddNewLabelItemViewHolder) {
        holder.addLabelItemBaseLayout.setOnClickListener {
            actionHandler(LabelAction.AddNew)
        }
    }

    override fun unbind(holder: AddNewLabelItemViewHolder) {
        holder.addLabelItemBaseLayout.setOnClickListener(null)
    }
}

class AddNewLabelItemViewHolder : KotlinEpoxyHolder() {
    val addLabelItemBaseLayout by bind<ConstraintLayout>(R.id.addLabelItemBaseLayout)
}