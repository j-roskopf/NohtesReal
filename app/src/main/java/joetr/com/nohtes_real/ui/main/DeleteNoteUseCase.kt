package joetr.com.nohtes_real.ui.main

import io.reactivex.Single
import joetr.com.data.NoteRepository
import joetr.com.data.entities.NoteEntity
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    fun delete(noteEntity: NoteEntity): Single<Int> {
        return noteRepository.delete(noteEntity)
    }
}