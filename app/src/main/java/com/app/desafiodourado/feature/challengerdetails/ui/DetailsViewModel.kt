package com.app.desafiodourado.feature.challengerdetails.ui

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.desafiodourado.core.utils.Result
import com.app.desafiodourado.feature.challengerdetails.domain.CompleteChallengerUseCase
import com.app.desafiodourado.feature.home.ui.model.Challenger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(private val completeChallengerUseCase: CompleteChallengerUseCase) :
    ViewModel() {
    private val viewModelState = MutableStateFlow(DetailsViewModelState())

    val uiState = viewModelState
        .map(DetailsViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    fun init(
        challengerList: List<Challenger.Card>,
        challengerSelected: Challenger.Card,
        userCoins: Int
    ) {
        viewModelState.update {
            it.copy(
                challengers = challengerList,
                selectedChallenger = challengerSelected,
                userCoins = userCoins
            )
        }
    }

    fun completeChallenger(
        challengerList: List<Challenger.Card>,
        challengerSelected: Challenger.Card,
        snackbarHostState: SnackbarHostState,
        userCoins: Int
    ) {
        viewModelScope.launch {
            if (userCoins < challengerSelected.value) {
                snackbarHostState.showSnackbar(INSUFFICIENT_QUANTITY)
            } else {
                handleCompleteChallenger(
                    challengerList = challengerList,
                    challengerSelected = challengerSelected,
                    userCoins = userCoins,
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }

    private suspend fun handleCompleteChallenger(
        challengerList: List<Challenger.Card>,
        challengerSelected: Challenger.Card,
        userCoins: Int,
        snackbarHostState: SnackbarHostState
    ) {
        when (val result = completeChallengerUseCase(
            challengerList = challengerList.toMutableList(),
            challengerSelected = challengerSelected,
            userCoins = userCoins
        )) {
            is Result.Success -> {
                viewModelState.update {
                    it.copy(selectedChallenger = result.data)
                }
            }

            is Result.Error -> {
                snackbarHostState.showSnackbar(ERROR)
            }
        }
    }

    companion object {
        private const val INSUFFICIENT_QUANTITY = "Quantidade de moedas insuficientes"
        private const val ERROR =
            "Ocorreu um problema ao completar esse desafio. Tente novamente, mais tarde."
    }
}