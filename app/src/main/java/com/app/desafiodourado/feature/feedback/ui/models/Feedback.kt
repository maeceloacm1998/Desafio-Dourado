package com.app.desafiodourado.feature.feedback.ui.models

data class Feedback(
    val feedbackText: String = "",
    val feedbackTypes: List<FeedbackTypes> = mutableListOf()
)