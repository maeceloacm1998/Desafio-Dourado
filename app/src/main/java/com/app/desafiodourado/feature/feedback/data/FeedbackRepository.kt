package com.app.desafiodourado.feature.feedback.data

import com.app.desafiodourado.feature.feedback.ui.models.Feedback
import com.google.firebase.firestore.DocumentSnapshot

interface FeedbackRepository {
    suspend fun handleFeedbackTypes(): Result<List<DocumentSnapshot>>
    suspend fun handleFinishFeedback(feedback: Feedback): Result<Boolean>
}