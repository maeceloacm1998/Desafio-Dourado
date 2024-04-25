package com.app.desafiodourado.feature.feedback.domain

import com.app.desafiodourado.feature.feedback.data.FeedbackRepository
import com.google.firebase.firestore.DocumentSnapshot

class GetFeedbackTypesUseCase(
    private val feedbackRepository: FeedbackRepository
) {
    suspend operator fun invoke(): Result<List<DocumentSnapshot>> = feedbackRepository.handleFeedbackTypes()
}