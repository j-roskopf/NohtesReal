package joetr.com.nohtes_real.ui.note.label

import io.reactivex.Maybe
import io.reactivex.rxkotlin.plusAssign
import joetr.com.data.entities.LabelEntity
import joetr.com.nohtes_real.android.base.BaseViewModel
import joetr.com.nohtes_real.ui.note.label.dataModels.LabelItem
import joetr.com.nohtes_real.ui.note.label.useCase.AddLabelUseCase
import joetr.com.nohtes_real.ui.note.label.useCase.DeleteLabelUseCase
import joetr.com.nohtes_real.ui.note.label.useCase.GetLabelsUseCase
import timber.log.Timber
import javax.inject.Inject

class LabelViewModel @Inject constructor(private val addLabelUseCase: AddLabelUseCase, private val getAllLabelsUseCase: GetLabelsUseCase, private val deleteLabelUseCase: DeleteLabelUseCase) : BaseViewModel<LabelState, LabelAction>() {

    fun addLabel(labelEntity: LabelEntity): Maybe<Long> {
        return addLabelUseCase.execute(labelEntity)
    }

    var allLabels: ArrayList<LabelItem> = arrayListOf()

    fun getLabels() {
        compositeDisposable += getAllLabelsUseCase.getAll()
            .subscribe({ list ->
                if (list.isNotEmpty()) {
                    allLabels.clear()
                    allLabels.add(LabelItem.AddNewLabelItem(LabelEntity("Add new", false)))
                    allLabels.addAll(list.map { LabelItem.UserEnteredLabelItem(it) })
                }
                state.onNext(LabelState.Content(allLabels))
            }, {
                Timber.e(it)
            })
    }

    fun deleteItemAtPosition(adapterPosition: Int) {
        val label = allLabels[adapterPosition].labelEntity
        // todo joe react to how deleting went
        compositeDisposable += deleteLabelUseCase.deleteLabel(label).subscribe()
        allLabels.removeAt(adapterPosition)
        state.onNext(LabelState.Content(allLabels))
    }

}