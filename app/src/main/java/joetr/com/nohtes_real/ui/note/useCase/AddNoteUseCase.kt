package joetr.com.nohtes_real.ui.note.useCase

import io.reactivex.Maybe
import joetr.com.data.NoteRepository
import joetr.com.data.entities.NoteEntity
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(private val noteRepository: NoteRepository) {

    fun insertNote(noteEntity: NoteEntity): Maybe<Long> {
        return noteRepository.insertNote(noteEntity)
    }

}