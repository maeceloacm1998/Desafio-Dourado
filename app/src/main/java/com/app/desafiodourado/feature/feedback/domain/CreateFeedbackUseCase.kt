package com.app.desafiodourado.feature.feedback.domain

import com.app.desafiodourado.feature.feedback.data.FeedbackRepository
import com.app.desafiodourado.feature.feedback.ui.models.Feedback
import com.app.desafiodourado.feature.feedback.ui.models.FeedbackTypes

class CreateFeedbackUseCase(
    private val feedbackRepository: FeedbackRepository
) {
    suspend operator fun invoke(
        feedbackText: String,
        feedbackTypes: List<FeedbackTypes>
    ): Result<Boolean> {
        val feedbackTypesChecked = feedbackTypes.filter { it.isSelected}
        return feedbackRepository.handleFinishFeedback(
            Feedback(
                feedbackText = feedbackText,
                feedbackTypes = feedbackTypesChecked
            )
        )
    }
}