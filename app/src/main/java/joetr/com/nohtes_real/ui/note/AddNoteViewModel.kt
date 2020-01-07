package joetr.com.nohtes_real.ui.note

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import joetr.com.data.entities.LabelEntity
import joetr.com.data.entities.NoteEntity
import joetr.com.nohtes_real.ui.note.label.useCase.GetLabelsUseCase
import joetr.com.nohtes_real.ui.note.useCase.AddNoteUseCase
import timber.log.Timber
import javax.inject.Inject

class AddNoteViewModel @Inject constructor(
    private val addToNoteUseCase: AddNoteUseCase,
    private val getLabelsUseCase: GetLabelsUseCase
) : ViewModel() {

    private val state: BehaviorSubject<AddNoteState> = BehaviorSubject.create()
    fun state(): Observable<AddNoteState> = state.hide()

    private val action: PublishSubject<AddNoteAction> = PublishSubject.create()
    fun action(): Observable<AddNoteAction> = action.hide()

    private val compositeDisposable = CompositeDisposable()

    var allLabels: ArrayList<LabelEntity> = arrayListOf()

    fun getLabels() {
        compositeDisposable += getLabelsUseCase.getAll()
            .subscribe({ list ->
                if (list.isNotEmpty()) {
                    allLabels = ArrayList(list)
                }
            }, {
                Timber.e(it)
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun toggleLabel(label: LabelEntity) {
        val index = allLabels.indexOfFirst { it.label == label.label }
        if (index >= 0 && index <= allLabels.size - 1) {
            allLabels[index] = allLabels[index].copy(checked = !label.checked)
        }
    }

    fun saveNote(contentAsHTML: String, noteEntity: NoteEntity?) {
        val checkedLabels = allLabels.filter { it.checked }
        val noteEntityToSave = noteEntity?.copy(content = contentAsHTML, labels = checkedLabels)
            ?: NoteEntity(
                id = 0,
                content = contentAsHTML,
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
            val index = allLabels.indexOfFirst { it.label == noteLabel.label }
            if(index >= 0) {
                allLabels[index].checked = true
            }
        }
    }
}