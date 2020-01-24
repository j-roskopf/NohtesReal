package joetr.com.nohtes_real.ui.main

import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxkotlin.plusAssign
import joetr.com.data.entities.NoteEntity
import joetr.com.nohtes_real.android.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(private val getNotesUseCase: GetNotesUseCase, private val deleteNoteUseCase: DeleteNoteUseCase) : BaseViewModel<MainPageState, MainPageAction>() {

    private var notes : ArrayList<NoteEntity> = arrayListOf()

    fun getAllNotes() {
        compositeDisposable += getNotesUseCase.getAll()
            .subscribe({
                if(it.isEmpty()) {
                    state.onNext(MainPageState.Empty)
                } else {
                    notes = ArrayList(it)
                    state.onNext(MainPageState.Content(notes))
                }
            }, {
                state.onNext(MainPageState.Error)
                Timber.e(it)
            })
    }

    fun deleteItemAtPosition(adapterPosition: Int) {
        if(adapterPosition == RecyclerView.NO_POSITION) return

        compositeDisposable += deleteNoteUseCase.delete(notes[adapterPosition])
            .subscribe({
                if(it >= 0) {
                    getAllNotes()
                } else {
                    state.onNext(MainPageState.Error)
                    Timber.e("Error deleting item")
                }
            }, {
                state.onNext(MainPageState.Error)
                Timber.e(it)
            })
    }
}

sealed class MainPageState {
    object Loading : MainPageState()
    object Error : MainPageState()
    data class Content(val data : List<NoteEntity>) : MainPageState()
    object Empty : MainPageState()
}

sealed class MainPageAction {
    object GetAllNotes : MainPageAction()
    data class NoteClicked(val noteEntity: NoteEntity) : MainPageAction()
}

typealias MainActionHandler = (MainPageAction) -> Unit