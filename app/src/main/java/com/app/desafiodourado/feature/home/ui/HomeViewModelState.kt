package com.app.desafiodourado.feature.home.ui

import com.app.desafiodourado.core.utils.ErrorMessage
import com.app.desafiodourado.feature.home.ui.model.Challenger

sealed interface HomeUiState {
    val isLoading: Boolean
    val isRefresh: Boolean
    val errorMessages: ErrorMessage?

    data class NoChallengers(
        override val isLoading: Boolean,
        override val isRefresh: Boolean,
        override val errorMessages: ErrorMessage?
    ) : HomeUiState

    data class HasChallengers(
        val challengers: Challenger,
        val selectedChallenger: Challenger.Card?,
        val badgeCount: Int,
        val finishAllMissions: Boolean,
        val showMissions: Boolean,
        val coin: Int,
        override val isLoading: Boolean,
        override val isRefresh: Boolean,
        override val errorMessages: ErrorMessage?,
    ) : HomeUiState
}

data class HomeViewModelState(
    val isLoading: Boolean = false,
    val isRefresh: Boolean = false,
    val errorMessages: ErrorMessage? = null,
    val challengers: Challenger? = null,
    val selectedChallenger: Challenger.Card? = null,
    val coin: Int = 0,
    val badgeCount: Int = 0,
    val finishAllMissions: Boolean = false,
    val showMissions: Boolean = false,
) {
    fun toUiState(): HomeUiState =
        if (challengers == null) {
            HomeUiState.NoChallengers(
                isLoading = isLoading,
                isRefresh = isRefresh,
                errorMessages = errorMessages,
            )
        } else {
            HomeUiState.HasChallengers(
                challengers = challengers,
                selectedChallenger = selectedChallenger,
                coin = coin,
                badgeCount = badgeCount,
                finishAllMissions = finishAllMissions,
                showMissions = showMissions,
                isLoading = isLoading,
                isRefresh = isRefresh,
                errorMessages = errorMessages,
            )
        }
}