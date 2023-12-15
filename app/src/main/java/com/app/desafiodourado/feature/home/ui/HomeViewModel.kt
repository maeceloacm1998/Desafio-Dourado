package com.app.desafiodourado.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.desafiodourado.R
import com.app.desafiodourado.core.utils.ErrorMessage
import com.app.desafiodourado.core.utils.Result.Error
import com.app.desafiodourado.core.utils.Result.Success
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.feature.home.data.HomeRepository
import com.app.desafiodourado.feature.home.domain.GetChallengersUseCase
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
    private val getChallengersUseCase: GetChallengersUseCase,
    val startCountDownUseCase: StartCountDownUseCase
) : ViewModel() {
    private val viewModelState = MutableStateFlow(HomeViewModelState(isLoading = true))

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

    private fun handleUpdateChallengers() {
        onLoading(true)

        viewModelScope.launch {
            when (val result = getChallengersUseCase()) {
                is Success -> {
                    onUpdateChallengerList(result.data)
                    onLoading(false)
                }

                is Error -> {
                    val errorMessages = ErrorMessage(
                        id = UUID.randomUUID().mostSignificantBits,
                        messageId = R.string.load_error
                    )
                    onUpdateMessageError(errorMessages)
                    onLoading(false)
                }
            }
        }
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

    fun refresh() {
        handleUpdateMissions()
        handleUpdateChallengers()
        handleUpdateCoins()
    }

    fun challengerSelected(challengers: Challenger.Card) {
        viewModelState.update { it.copy(selectedChallenger = challengers) }
    }

    fun onInteractionFeed() {
        viewModelState.update { it.copy(selectedChallenger = null) }
        refresh()
    }

    fun openMissions(visible: Boolean) {
        viewModelState.update { it.copy(showMissions = visible) }
    }

    private fun onUpdateCoin(coin: Int) {
        viewModelState.update { it.copy(coin = coin) }
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
}