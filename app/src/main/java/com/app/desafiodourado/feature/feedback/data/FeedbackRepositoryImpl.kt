package com.app.desafiodourado.feature.feedback.data

import com.app.desafiodourado.core.firebase.FirebaseClient
import com.app.desafiodourado.core.firebase.FirebaseConstants.Collections.FEEDBACK
import com.app.desafiodourado.core.firebase.FirebaseConstants.Collections.FEEDBACK_TYPES
import com.app.desafiodourado.feature.feedback.ui.models.Feedback
import com.google.firebase.firestore.DocumentSnapshot

class FeedbackRepositoryImpl(
    private val firebaseClient: FirebaseClient
) : FeedbackRepository {
    override suspend fun handleFeedbackTypes(): Result<List<DocumentSnapshot>> {
        return firebaseClient.getDocumentValue(collectionPath = FEEDBACK_TYPES)
    }

    override suspend fun handleFinishFeedback(feedback: Feedback): Result<Boolean> {
        firebaseClient.createDocument(collectionPath = FEEDBACK, data = feedback)
            .onSuccess {
                return Result.success(true)
            }

        return Result.failure(Throwable("Error to create feedback"))
    }
}