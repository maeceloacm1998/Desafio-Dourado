package com.app.desafiodourado.feature.feedback.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val getFeedbackTypesUseCase: GetFeedbackTypesUseCase
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

    fun onChangeFeedbackTextUpdate(feedbackText: String) {
        viewModelState.update { it.copy(feedbackText = feedbackText) }
    }

    fun onFinishFeedback() {
        onLoadingFinishFeedback(true)
        // TODO: chamar um caso de uso para salvar o feedback no firebase
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
                onError(true)
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

    private fun onError(state: Boolean) {
        viewModelState.update { it.copy(isErrorFetchData = state) }
    }
}