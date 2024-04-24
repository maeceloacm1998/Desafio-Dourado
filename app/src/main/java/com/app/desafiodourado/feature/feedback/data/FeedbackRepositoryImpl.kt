package com.app.desafiodourado.feature.feedback.data

import com.app.desafiodourado.core.firebase.FirebaseClient
import com.app.desafiodourado.core.firebase.FirebaseConstants.Collections.FEEDBACK_TYPES
import com.google.firebase.firestore.DocumentSnapshot

class FeedbackRepositoryImpl(
    private val firebaseClient: FirebaseClient
): FeedbackRepository {
    override suspend fun handleFeedbackTypes(): Result<List<DocumentSnapshot>> {
        return firebaseClient.getDocumentValue(collectionPath = FEEDBACK_TYPES)
    }
}