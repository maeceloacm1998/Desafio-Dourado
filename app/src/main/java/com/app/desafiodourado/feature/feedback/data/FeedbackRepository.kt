package com.app.desafiodourado.feature.feedback.data

import com.google.firebase.firestore.DocumentSnapshot

interface FeedbackRepository {
    suspend fun handleFeedbackTypes(): Result<List<DocumentSnapshot>>
}