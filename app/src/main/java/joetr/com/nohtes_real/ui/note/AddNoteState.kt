package joetr.com.nohtes_real.ui.note

sealed class AddNoteState {
    object Loading: AddNoteState()
    object AddingNote : AddNoteState()
}

sealed class AddNoteAction {
    object Error : AddNoteAction()
    object NoteAddedSuccessfully : AddNoteAction()
}