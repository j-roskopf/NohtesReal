package joetr.com.nohtes_real.ui.note.label.useCase

import io.reactivex.Maybe
import joetr.com.data.LabelRepository
import joetr.com.data.entities.LabelEntity
import javax.inject.Inject

class AddLabelUseCase @Inject constructor(private val labelRepository: LabelRepository) {
    fun execute(labelEntity: LabelEntity): Maybe<Long> {
        return labelRepository.add(labelEntity)
    }
}