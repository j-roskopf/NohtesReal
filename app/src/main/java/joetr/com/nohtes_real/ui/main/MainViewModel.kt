package joetr.com.nohtes_real.ui.main

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import joetr.com.data.entities.NoteEntity
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(private val getNotesUseCase: GetNotesUseCase) : ViewModel() {

    private val state: BehaviorSubject<MainPageState> = BehaviorSubject.create()
    fun state(): Observable<MainPageState> = state.hide()

    private val action: PublishSubject<MainPageAction> = PublishSubject.create()
    fun action(): Observable<MainPageAction> = action.hide()

    private val compositeDisposable = CompositeDisposable()

    fun getAllNotes() {
        compositeDisposable += getNotesUseCase.getAll()
            .subscribe({
                state.onNext(MainPageState.Content(it))
            }, {
                state.onNext(MainPageState.Error)
                Timber.e(it)
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

sealed class MainPageState {
    object Loading : MainPageState()
    object Error : MainPageState()
    data class Content(val data : List<NoteEntity>) : MainPageState()
}

sealed class MainPageAction {
    object GetAllNotes : MainPageAction()
    data class NoteClicked(val noteEntity: NoteEntity) : MainPageAction()
}

typealias MainActionHandler = (MainPageAction) -> Unit