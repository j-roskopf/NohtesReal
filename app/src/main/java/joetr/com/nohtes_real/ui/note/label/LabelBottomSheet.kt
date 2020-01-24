package joetr.com.nohtes_real.ui.note.label

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyTouchHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import joetr.com.data.entities.LabelEntity
import joetr.com.nohtes_real.R
import joetr.com.nohtes_real.android.base.BaseBottomSheetDialogFragment
import joetr.com.nohtes_real.android.extensions.exhaustive
import joetr.com.nohtes_real.di.component.FragmentComponent
import joetr.com.nohtes_real.ui.note.label.dataModels.LabelItem
import joetr.com.nohtes_real.ui.note.label.epoxyModels.LabelItemView
import kotlinx.android.synthetic.main.add_label_dialog.view.*
import kotlinx.android.synthetic.main.label_bottom_sheet.*
import kotlinx.android.synthetic.main.link_input_dialog.view.linkAddButton
import timber.log.Timber
import javax.inject.Inject

class LabelBottomSheet : BaseBottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: LabelViewModel

    private val controller = LabelController()

    lateinit var labelInteraction: LabelInteraction

    var labels : List<LabelItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[LabelViewModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog.window != null) {
            dialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            dialog.setCancelable(false)
        }
        return dialog
    }

    private fun render(state: LabelState) {
        when(state) {
            is LabelState.Content -> {
                viewModel.allLabels = ArrayList(state.data)
                controller.setData(state.data, ::handle)
            }
        }.exhaustive
    }

    private fun handle(action: LabelAction) {
        when(action) {
            is LabelAction.LabelClicked -> {
                labelInteraction.labelClicked(action.label)
            }
            LabelAction.AddNew -> showAddLabelDialog()
        }.exhaustive
    }

    private fun showAddLabelDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.DayNightAlertDialog)

        val inflater: LayoutInflater = LayoutInflater.from(context)
        val dialogView: View = inflater.inflate(R.layout.add_label_dialog, null)
        builder.setView(dialogView)

        val dialog: AlertDialog = builder.create()

        dialogView.linkAddButton.setOnClickListener {
            val labelName = dialogView.labelName.text.toString()
            if(labelName.isNotEmpty()) {
                val labelEntity = LabelEntity(labelName, false)
                compositeDisposable += viewModel.addLabel(labelEntity)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        dialog.dismiss()
                        labelInteraction.labelAdded(labelEntity)
                        viewModel.getLabels()
                    }, {
                        Timber.e(it)
                        dialog.dismiss()
                        dismiss()
                    })
            }
        }

        dialog.show()
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

        render(LabelState.Content(labels))

        EpoxyTouchHelper.initSwiping(labelRecyclerView)
            .leftAndRight()
            .withTarget(LabelItemView::class.java)
            .andCallbacks(object: EpoxyTouchHelper.SwipeCallbacks<LabelItemView>() {
                override fun onSwipeCompleted(
                    model: LabelItemView?,
                    itemView: View?,
                    position: Int,
                    direction: Int
                ) {
                    labelInteraction.labelDeleted(viewModel.allLabels[position].labelEntity)
                    viewModel.deleteItemAtPosition(position)
                }
            })

        compositeDisposable += viewModel.action()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::handle) { Timber.e(it) }

        compositeDisposable += viewModel.state()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::render) { Timber.e(it) }


    }

    companion object {
        const val TAG = "LabelBottomSheet"
    }
}

sealed class LabelAction {
    data class LabelClicked(val label: LabelEntity) : LabelAction()
    object AddNew : LabelAction()
}

sealed class LabelState {
    data class Content(val data: List<LabelItem>) : LabelState()
}

typealias LabelActionHandler = (LabelAction) -> Unit