package joetr.com.nohtes_real.ui.note.label

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import joetr.com.data.entities.LabelEntity
import joetr.com.nohtes_real.R
import joetr.com.nohtes_real.android.base.BaseBottomSheetDialogFragment
import joetr.com.nohtes_real.android.extensions.exhaustive
import joetr.com.nohtes_real.di.component.FragmentComponent
import kotlinx.android.synthetic.main.label_bottom_sheet.*
import javax.inject.Inject


class LabelBottomSheet : BaseBottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val controller = LabelController()

    lateinit var labelInteraction: LabelInteraction

    lateinit var labels : List<LabelEntity>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog.window != null) {
            dialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            dialog.setCancelable(false)
        }
        return dialog
    }

    private fun handle(action: LabelAction) {
        when(action) {
            is LabelAction.LabelClicked -> {
                labelInteraction.labelClicked(action.label)
            }
        }.exhaustive
    }

    private fun displayContent() {
        controller.setData(labels, ::handle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.label_bottom_sheet, container, false)
    }

    override fun injectSelf(component: FragmentComponent) = component.inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        labelRecyclerView.adapter = controller.adapter
        labelRecyclerView.layoutManager = LinearLayoutManager(context)

        displayContent()
    }

    companion object {
        const val TAG = "LabelBottomSheet"
    }
}

sealed class LabelAction {
    data class LabelClicked(val label: LabelEntity) : LabelAction()
}

typealias LabelActionHandler = (LabelAction) -> Unit