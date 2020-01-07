package joetr.com.nohtes_real.ui.main

import io.reactivex.Maybe
import joetr.com.data.NoteRepository
import joetr.com.data.entities.NoteEntity
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(private val noteRepository: NoteRepository) {

    fun getAll(): Maybe<List<NoteEntity>> {
        return noteRepository.getAll()
    }

}