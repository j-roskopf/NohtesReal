package joetr.com.nohtes_real.ui.note.label.useCase

import io.reactivex.Single
import joetr.com.data.LabelRepository
import joetr.com.data.entities.LabelEntity
import javax.inject.Inject

class DeleteLabelUseCase @Inject constructor(private val labelRepository: LabelRepository) {
    fun deleteLabel(labelEntity: LabelEntity): Single<Int> {
        return labelRepository.delete(labelEntity)
    }
}