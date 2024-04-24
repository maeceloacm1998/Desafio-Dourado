package com.app.desafiodourado.feature.feedback.ui

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.desafiodourado.components.snackbar.SnackbarCustomType.ERROR
import com.app.desafiodourado.components.snackbar.SnackbarCustomType.SUCCESS
import com.app.desafiodourado.feature.feedback.domain.CreateFeedbackUseCase
import com.app.desafiodourado.feature.feedback.domain.GetFeedbackTypesUseCase
import com.app.desafiodourado.feature.feedback.ui.models.FeedbackTypes
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeedbackViewModel(
    private val getFeedbackTypesUseCase: GetFeedbackTypesUseCase,
    private val createFeedbackUseCase: CreateFeedbackUseCase
) : ViewModel() {
    private val viewModelState = MutableStateFlow(FeedbackViewModelState(isLoading = true))

    val uiState = viewModelState
        .map(FeedbackViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        viewModelScope.launch {
            handleFeedbackTypes()
        }
    }

    private suspend fun handleFeedbackTypes() {
        getFeedbackTypesUseCase()
            .onSuccess {
                val feedbackTypesList = it.map { value ->
                    checkNotNull(value.toObject<FeedbackTypes>())
                }
                onUpdateFeedbackTypesList(feedbackTypesList)
                onLoading(false)
            }
            .onFailure {
                onLoading(false)
            }
    }

    private suspend fun handleFinishFeedback(
        goBackScreen: () -> Unit,
        snackbarHostState: SnackbarHostState,
        feedbackText: String,
        feedbackTypes: List<FeedbackTypes>
    ) {
        createFeedbackUseCase(
            feedbackText = feedbackText,
            feedbackTypes = feedbackTypes
        )
            .onSuccess {
                onSetSnackBarSuccess(snackbarHostState)
                onLoadingFinishFeedback(false)
                goBackScreen()
            }
            .onFailure {
                onSetSnackBarError(snackbarHostState)
                onLoadingFinishFeedback(false)
            }
    }


    fun onFinishFeedback(
        snackbarHostState: SnackbarHostState,
        goBackScreen: () -> Unit
    ) {
        onLoadingFinishFeedback(true)
        val feedbackType = onHasFeedbackTypesSelected()
        val feedbackText = onHasFeedbackText()

        if (feedbackType && feedbackText) {
            viewModelScope.launch {
                handleFinishFeedback(
                    goBackScreen = goBackScreen,
                    snackbarHostState = snackbarHostState,
                    feedbackText = viewModelState.value.feedbackText,
                    feedbackTypes = viewModelState.value.feedbackTypesList
                )
            }
        } else {
            onLoadingFinishFeedback(false)
        }
    }

    private fun onHasFeedbackTypesSelected(): Boolean {
        val containsFeedbackTypesSelected =
            viewModelState.value.feedbackTypesList.filter { it.isSelected }

        return if (containsFeedbackTypesSelected.isEmpty()) {
            onErrorFeedbackType(true)
            false
        } else {
            true
        }
    }

    private fun onHasFeedbackText(): Boolean {
        return if (viewModelState.value.feedbackText.isEmpty()) {
            onErrorFeedbackText(true)
            false
        } else {
            true
        }
    }

    fun onCheckFeedbackType(feedbackType: FeedbackTypes) {
        val feedbackTypesList = viewModelState.value.feedbackTypesList.map { value ->
            onErrorFeedbackType(false)
            if (value.feedbackType == feedbackType.feedbackType) {
                value.copy(isSelected = !value.isSelected)
            } else {
                value
            }
        }
        onUpdateFeedbackTypesList(feedbackTypesList)
    }

    fun onChangeFeedbackTextUpdate(feedbackText: String) {
        viewModelState.update {
            it.copy(
                feedbackText = feedbackText,
                isErrorFeedbackTextIsEmpty = false
            )
        }
    }

    private fun onUpdateFeedbackTypesList(feedbackTypesList: List<FeedbackTypes>) {
        viewModelState.update { it.copy(feedbackTypesList = feedbackTypesList) }
    }

    private fun onLoadingFinishFeedback(state: Boolean) {
        viewModelState.update { it.copy(isLoadingFinishFeedback = state) }
    }

    private fun onLoading(state: Boolean) {
        viewModelState.update { it.copy(isLoading = state) }
    }

    private fun onErrorFeedbackType(state: Boolean) {
        viewModelState.update { it.copy(isErrorFeedbackTypesNotSelected = state) }
    }

    private fun onErrorFeedbackText(state: Boolean) {
        viewModelState.update { it.copy(isErrorFeedbackTextIsEmpty = state) }
    }

    private suspend fun onSetSnackBarError(snackbarHostState: SnackbarHostState) {
        viewModelState.update { it.copy(snackBarType = ERROR) }
        snackbarHostState.showSnackbar(
            message = FEEDBACK_SNACK_BAR_ERROR,
            duration = SnackbarDuration.Short
        )
    }

    private suspend fun onSetSnackBarSuccess(snackbarHostState: SnackbarHostState) {
        viewModelState.update { it.copy(snackBarType = SUCCESS) }
        snackbarHostState.showSnackbar(
            message = FEEDBACK_SNACK_BAR_SUCCESS,
            duration = SnackbarDuration.Short
        )
    }

    companion object {
        const val FEEDBACK_SNACK_BAR_ERROR = "Erro ao enviar seu feedback, tente novamente."
        const val FEEDBACK_SNACK_BAR_SUCCESS = "Feedback enviado com sucesso."
    }
}