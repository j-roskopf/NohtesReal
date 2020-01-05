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
class AddNoteViewModel @Inject constructor(private val addToNoteUseCase: AddNoteUseCase,
                                           private val getLabelsUseCase: GetLabelsUseCase
) : ViewModel() {

    private val state: BehaviorSubject<AddNoteState> = BehaviorSubject.create()
    fun state(): Observable<AddNoteState> = state.hide()

    private val action: PublishSubject<AddNoteAction> = PublishSubject.create()
    fun action(): Observable<AddNoteAction> = action.hide()

    private val compositeDisposable = CompositeDisposable()

    var labels : ArrayList<LabelEntity> = arrayListOf()

    fun getLabels() {
        compositeDisposable += getLabelsUseCase.getAll()
            .subscribe({ list ->
                if(list.isNotEmpty()) {
                    labels = ArrayList(list)
                }
            }, {
                Timber.e(it)
            })
    }

    fun insertNote(noteEntity: NoteEntity) {
        compositeDisposable += addToNoteUseCase.insertNote(noteEntity)
            .subscribe({
                if(it >= 0) {
                    action.onNext(AddNoteAction.NoteAddedSuccessfully)
                } else {
                    action.onNext(AddNoteAction.Error)
                }
            }, {
                action.onNext(AddNoteAction.Error)
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun toggleLabel(label: LabelEntity) {
        if(label.checked) {
            removeLabel(label)
        } else {
            val index = labels.indexOf(label)
            if(index >= 0 && index < labels.size - 1) {
                labels[index] = labels[index].copy(checked = true)
            }
        }
    }

    fun removeLabel(label: LabelEntity) {
        labels.remove(label)
    }
}