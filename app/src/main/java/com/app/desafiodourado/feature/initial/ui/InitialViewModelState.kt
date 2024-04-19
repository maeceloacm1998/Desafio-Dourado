package com.app.desafiodourado.feature.initial.ui

sealed interface InitialViewModelUiState {
    val isLoading: Boolean
    data class Data(
        override val isLoading: Boolean,
        val isUserNameError: Boolean
    ) : InitialViewModelUiState
}

data class InitialViewModelState(
    val isLoading: Boolean = false,
    val isUserNameError: Boolean = false
) {
    fun toUiState(): InitialViewModelUiState =
        InitialViewModelUiState.Data(
            isLoading = isLoading,
            isUserNameError = isUserNameError
        )
}