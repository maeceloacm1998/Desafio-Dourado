package com.app.desafiodourado.feature.feedback.ui

import com.app.desafiodourado.components.snackbar.SnackbarCustomType
import com.app.desafiodourado.feature.feedback.ui.FeedbackViewModelUiState.HasFeedbackTypes
import com.app.desafiodourado.feature.feedback.ui.FeedbackViewModelUiState.NoHasFeedbackTypes
import com.app.desafiodourado.feature.feedback.ui.models.FeedbackTypes

sealed interface FeedbackViewModelUiState {
    val isLoading: Boolean

    data class NoHasFeedbackTypes(
        override val isLoading: Boolean,
    ): FeedbackViewModelUiState

    data class HasFeedbackTypes(
        override val isLoading: Boolean,
        val isErrorFeedbackTypesNotSelected: Boolean,
        val isErrorFeedbackTextIsEmpty: Boolean,
        val isLoadingFinishFeedback: Boolean,
        val snackBarType: SnackbarCustomType,
        val feedbackText: String,
        val feedbackTypesList: List<FeedbackTypes>
    ) : FeedbackViewModelUiState
}

data class FeedbackViewModelState(
    val isLoading: Boolean = false,
    val isErrorFeedbackTypesNotSelected: Boolean = false,
    val isErrorFeedbackTextIsEmpty: Boolean = false,
    val isLoadingFinishFeedback: Boolean = false,
    val snackBarType: SnackbarCustomType = SnackbarCustomType.SUCCESS,
    val feedbackTypesList: List<FeedbackTypes> = mutableListOf(),
    val feedbackText: String = ""
) {
    fun toUiState(): FeedbackViewModelUiState =
        if(feedbackTypesList.isEmpty()) {
            NoHasFeedbackTypes(isLoading = isLoading)
        } else {
            HasFeedbackTypes(
                isLoading = isLoading,
                isErrorFeedbackTypesNotSelected = isErrorFeedbackTypesNotSelected,
                isErrorFeedbackTextIsEmpty = isErrorFeedbackTextIsEmpty,
                isLoadingFinishFeedback = isLoadingFinishFeedback,
                snackBarType = snackBarType,
                feedbackTypesList = feedbackTypesList,
                feedbackText = feedbackText
            )
        }
}