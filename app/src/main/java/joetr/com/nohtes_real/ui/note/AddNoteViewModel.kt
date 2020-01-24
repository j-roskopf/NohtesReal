package joetr.com.nohtes_real.ui.note

import io.reactivex.rxkotlin.plusAssign
import joetr.com.data.entities.LabelEntity
import joetr.com.data.entities.NoteEntity
import joetr.com.nohtes_real.android.base.BaseViewModel
import joetr.com.nohtes_real.ui.note.label.dataModels.LabelItem
import joetr.com.nohtes_real.ui.note.label.useCase.GetLabelsUseCase
import joetr.com.nohtes_real.ui.note.useCase.AddNoteUseCase
import timber.log.Timber
import xute.markdeditor.models.DraftModel
import javax.inject.Inject

class AddNoteViewModel @Inject constructor(
    private val addToNoteUseCase: AddNoteUseCase,
    private val getLabelsUseCase: GetLabelsUseCase
) : BaseViewModel<AddNoteState, AddNoteAction>() {

    var userEnteredLabels: ArrayList<LabelEntity> = arrayListOf()

    fun getLabels() {
        compositeDisposable += getLabelsUseCase.getAll()
            .subscribe({ list ->
                if (list.isNotEmpty()) {
                    userEnteredLabels = ArrayList(list)
                }
            }, {
                Timber.e(it)
            })
    }

    fun toggleLabel(label: LabelEntity) {
        val index = userEnteredLabels.indexOfFirst { it.label == label.label }
        if (index >= 0 && index <= userEnteredLabels.size - 1) {
            userEnteredLabels[index] = userEnteredLabels[index].copy(checked = !label.checked)
        }
    }

    fun saveNote(content: DraftModel, markDown: String, noteEntity: NoteEntity?) {
        if(content.items == null) return

        val checkedLabels = userEnteredLabels.filter { it.checked }

        val noteEntityToSave = noteEntity?.copy(draftContent = content.items!!, markDown = markDown, labels = checkedLabels)
            ?: NoteEntity(
                id = 0,
                markDown = markDown,
                draftContent = content.items!!,
                timestamp = System.currentTimeMillis(),
                labels = checkedLabels
            )
        compositeDisposable += addToNoteUseCase.insertNote(
            noteEntityToSave
        ).subscribe({
            if (it > 0) {
                action.onNext(AddNoteAction.NoteAddedSuccessfully)
            } else {
                action.onNext(AddNoteAction.Error)
            }
        }, {
            action.onNext(AddNoteAction.NoteAddedSuccessfully)
            Timber.e(it)
        })
    }

    fun addTags(noteEntity: NoteEntity) {
        noteEntity.labels?.forEach { noteLabel ->
            val index = userEnteredLabels.indexOfFirst { it.label == noteLabel.label }
            if(index >= 0) {
                userEnteredLabels[index].checked = true
            }
        }
    }

    fun getLabelsForBottomSheet(): List<LabelItem> {
        return listOf(LabelItem.AddNewLabelItem(LabelEntity("add new", false))).plus(userEnteredLabels.map {
            LabelItem.UserEnteredLabelItem(it)
        })
    }

    fun addLabel(label: LabelEntity) {
        userEnteredLabels.add(label)
    }

    fun deleteLabel(label: LabelEntity) {
        userEnteredLabels.remove(label)
        print("test")
    }
}