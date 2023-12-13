package com.app.desafiodourado.feature.home.ui

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.desafiodourado.R
import com.app.desafiodourado.core.utils.ErrorMessage
import com.app.desafiodourado.core.utils.Result.Success
import com.app.desafiodourado.core.utils.Result.Error
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.feature.home.data.HomeRepository
import com.app.desafiodourado.feature.home.domain.StartCountDownUseCase
import com.app.desafiodourado.feature.home.domain.UpdateMissionsUseCase
import com.app.desafiodourado.feature.home.domain.UpdateQuantityCoinsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val updateMissionsUseCase: UpdateMissionsUseCase,
    private val updateQuantityCoinsUseCase: UpdateQuantityCoinsUseCase,
    startCountDownUseCase: StartCountDownUseCase
) : ViewModel() {
    private val viewModelState = MutableStateFlow(HomeViewModelState(isLoading = true))
    private var challengerList: MutableList<Challenger.Card> = mutableListOf()

    val uiState = viewModelState
        .map(HomeViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        startCountDownUseCase()
        handleUpdateMissions()
        handleUpdateChallengers()
        handleUpdateCoins()
        observables()
    }

    private fun handleUpdateMissions() {
        viewModelScope.launch {
            updateMissionsUseCase()
        }
    }

    private fun handleUpdateCoins() {
        viewModelScope.launch {
            homeRepository.updateCoins()
        }
    }

    fun handleUpdateChallengers() {
        onLoading(true)

        viewModelScope.launch {
            when (val result = homeRepository.getChallengers()) {
                is Success -> {
                    challengerList = result.data.challengers.toMutableList()
                    onUpdateChallengerList(challenger = result.data)
                }

                is Error -> {
                    val errorMessages = ErrorMessage(
                        id = UUID.randomUUID().mostSignificantBits,
                        messageId = R.string.load_error
                    )
                    onUpdateMessageError(errorMessages)
                }
            }
        }

        onLoading(false)
    }

    private fun observables() {
        viewModelScope.launch {
            homeRepository.observeCoins().collect { coin ->
                onUpdateCoin(coin)
            }
        }

        viewModelScope.launch {
            homeRepository.observeMissions().collect { missions ->
                val missionsNotChecked = missions.filter { mission -> !mission.isChecked }.size
                val isFinishAllMissions = missionsNotChecked == 0

                if (missions.isNotEmpty()) {
                    onUpdateBadgeMissions(
                        badgeCount = missionsNotChecked,
                        isFinishAllMissions = isFinishAllMissions
                    )
                }
            }
        }
    }

    fun completedChallenger(
        challengerSelected: Challenger.Card,
        snackbarHostState: SnackbarHostState
    ) {
        viewModelScope.launch {
            val userCoins = homeRepository.getCoins()

            if (userCoins < challengerSelected.value) {
                snackbarHostState.showSnackbar(INSUFFICIENT_QUANTITY)
            } else {
                handleCompleteChallenger(
                    userCoins = userCoins,
                    challengerSelected = challengerSelected,
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }

    private suspend fun handleCompleteChallenger(
        userCoins: Int,
        challengerSelected: Challenger.Card,
        snackbarHostState: SnackbarHostState
    ) {
        val existChallenger = challengerList.contains(challengerSelected)

        if (existChallenger) {
            val challengerIndex = challengerList.indexOf(challengerSelected)
            challengerList[challengerIndex] = challengerList[challengerIndex].copy(complete = true)

            when (val result = homeRepository.completeChallenger(challengerList)) {
                is Success -> {
                    val newQuantityCoins = userCoins - challengerSelected.value
                    onUpdateCoin(newQuantityCoins)
                    onUpdateChallengerList(
                        challenger = result.data,
                        challengerSelected = challengerSelected
                    )
                }

                is Error -> {
                    snackbarHostState.showSnackbar(ERROR)
                }
            }
        }
    }

    fun challengerSelected(challengers: Challenger.Card) {
        viewModelState.update { it.copy(selectedChallenger = challengers) }
    }

    fun onInteractionFeed() {
        viewModelState.update { it.copy(selectedChallenger = null) }
    }

    fun openMissions(visible: Boolean) {
        viewModelState.update { it.copy(showMissions = visible) }
    }

    private fun onUpdateCoin(coin: Int) {
        viewModelScope.launch {
            updateQuantityCoinsUseCase(coin)
                .onSuccess { viewModelState.update { it.copy(coin = coin) } }
        }
    }

    private fun onUpdateChallengerList(
        challenger: Challenger,
        challengerSelected: Challenger.Card? = null
    ) {
        viewModelState.update {
            it.copy(
                challengers = challenger,
                selectedChallenger = challengerSelected?.copy(complete = true)
            )
        }
    }

    private fun onUpdateBadgeMissions(
        badgeCount: Int,
        isFinishAllMissions: Boolean
    ) {
        viewModelState.update {
            it.copy(
                badgeCount = badgeCount,
                finishAllMissions = isFinishAllMissions
            )
        }
    }

    private fun onUpdateMessageError(errorMessages: ErrorMessage) {
        viewModelState.update { it.copy(errorMessages = errorMessages) }
    }

    private fun onLoading(state: Boolean) {
        viewModelState.update { it.copy(isLoading = state) }
    }

    companion object {
        private const val INSUFFICIENT_QUANTITY = "Quantidade de moedas insuficientes"
        private const val ERROR = "Erro ao completar esse desafio."
    }
}