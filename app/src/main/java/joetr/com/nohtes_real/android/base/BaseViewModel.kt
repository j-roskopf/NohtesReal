package joetr.com.nohtes_real.android.base

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel<S, A> : ViewModel() {

    protected val state: BehaviorSubject<S> = BehaviorSubject.create()
    fun state(): Observable<S> = state.hide()

    protected val action: PublishSubject<A> = PublishSubject.create()
    fun action(): Observable<A> = action.hide()

    val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}