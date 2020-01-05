package joetr.com.nohtes_real.ui.note.label.epoxyModels

import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import joetr.com.data.entities.LabelEntity
import joetr.com.nohtes_real.R
import joetr.com.nohtes_real.android.extensions.KotlinEpoxyHolder
import joetr.com.nohtes_real.ui.note.label.LabelAction
import joetr.com.nohtes_real.ui.note.label.LabelActionHandler

@EpoxyModelClass(layout = R.layout.label_item)
abstract class LabelItemView : EpoxyModelWithHolder<LabelItemViewHolder>() {

    @EpoxyAttribute
    lateinit var labelEntity: LabelEntity

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var actionHandler: LabelActionHandler

    override fun bind(holder: LabelItemViewHolder) {
        holder.labelItemText.text = labelEntity.label
        holder.labelItemCheck.isChecked = labelEntity.checked
        holder.labelItemBaseLayout.setOnClickListener {
            holder.labelItemCheck.performClick()
        }
        holder.labelItemCheck.setOnClickListener {
            actionHandler(LabelAction.LabelClicked(labelEntity))
        }
    }

    override fun unbind(holder: LabelItemViewHolder) {
        holder.labelItemBaseLayout.setOnClickListener(null)
        holder.labelItemCheck.setOnClickListener(null)
    }
}

class LabelItemViewHolder : KotlinEpoxyHolder() {
    val labelItemText by bind<TextView>(R.id.labelItemText)
    val labelItemBaseLayout by bind<ConstraintLayout>(R.id.labelItemBaseLayout)
    val labelItemCheck by bind<CheckBox>(R.id.labelItemCheck)
}