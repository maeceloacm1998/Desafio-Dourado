package com.app.desafiodourado.feature.challengerdetails.ui

import com.app.desafiodourado.core.utils.ErrorMessage
import com.app.desafiodourado.feature.home.ui.model.Challenger

sealed interface DetailsUiState {
    val isLoading: Boolean
    val errorMessages: ErrorMessage?

    data class HasChallengersDetails(
        val challengers: List<Challenger.Card>,
        val selectedChallenger: Challenger.Card,
        val userCoins: Int,
        override val isLoading: Boolean,
        override val errorMessages: ErrorMessage?,
    ) : DetailsUiState
}

data class DetailsViewModelState(
    val challengers: List<Challenger.Card> = mutableListOf(),
    val selectedChallenger: Challenger.Card = Challenger.Card(),
    val userCoins: Int = 0,
    val isLoading: Boolean = false,
    val errorMessages: ErrorMessage? = null,
) {
    fun toUiState(): DetailsUiState = DetailsUiState.HasChallengersDetails(
        challengers = challengers,
        selectedChallenger = selectedChallenger,
        userCoins = userCoins,
        isLoading = isLoading,
        errorMessages = errorMessages,
    )
}