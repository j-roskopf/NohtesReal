package joetr.com.nohtes_real.ui.note.label.useCase

import io.reactivex.Maybe
import joetr.com.data.LabelRepository
import joetr.com.data.entities.LabelEntity
import javax.inject.Inject

class GetLabelsUseCase @Inject constructor(private val labelRepository: LabelRepository) {
    fun getAll(): Maybe<List<LabelEntity>> {
        return labelRepository.getAllLabels()
    }
}