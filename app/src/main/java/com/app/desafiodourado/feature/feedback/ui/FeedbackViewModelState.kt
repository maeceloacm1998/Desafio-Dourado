package com.app.desafiodourado.feature.feedback.ui

import com.app.desafiodourado.feature.feedback.ui.FeedbackViewModelUiState.HasFeedbackTypes
import com.app.desafiodourado.feature.feedback.ui.FeedbackViewModelUiState.NoHasFeedbackTypes
import com.app.desafiodourado.feature.feedback.ui.models.FeedbackTypes

sealed interface FeedbackViewModelUiState {
    val isLoading: Boolean
    val isErrorFetchData: Boolean

    data class NoHasFeedbackTypes(
        override val isLoading: Boolean,
        override val isErrorFetchData: Boolean
    ): FeedbackViewModelUiState

    data class HasFeedbackTypes(
        override val isLoading: Boolean,
        override val isErrorFetchData: Boolean,
        val isLoadingFinishFeedback: Boolean,
        val feedbackText: String,
        val feedbackTypesList: List<FeedbackTypes>
    ) : FeedbackViewModelUiState
}

data class FeedbackViewModelState(
    val isLoading: Boolean = false,
    val isErrorFetchData: Boolean = false,
    val isLoadingFinishFeedback: Boolean = false,
    val feedbackText: String = "",
    val feedbackTypesList: List<FeedbackTypes> = mutableListOf()
) {
    fun toUiState(): FeedbackViewModelUiState =
        if(feedbackTypesList.isEmpty()) {
            NoHasFeedbackTypes(
                isLoading = isLoading,
                isErrorFetchData = isErrorFetchData
            )
        } else {
            HasFeedbackTypes(
                isLoading = isLoading,
                isErrorFetchData = isErrorFetchData,
                isLoadingFinishFeedback = isLoadingFinishFeedback,
                feedbackText = feedbackText,
                feedbackTypesList = feedbackTypesList
            )
        }
}