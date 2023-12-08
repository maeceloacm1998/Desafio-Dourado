package com.app.desafiodourado.feature.home.ui

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.desafiodourado.R
import com.app.desafiodourado.core.accountmanager.AccountManager
import com.app.desafiodourado.core.utils.DateUtils.compareDates
import com.app.desafiodourado.core.utils.DateUtils.getCurrentDate
import com.app.desafiodourado.core.utils.ErrorMessage
import com.app.desafiodourado.core.utils.Result.Success
import com.app.desafiodourado.core.utils.Result.Error
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.feature.home.data.HomeRepository
import com.app.desafiodourado.feature.home.ui.model.Missions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val accountManager: AccountManager
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
        handleUpdateMissions()
        updateChallengers()
        observables()
    }

    private fun handleUpdateMissions() {
        viewModelScope.launch {
            val lastUpdateMissionDate = accountManager.getUserLogged().lastUpdateMissions
            if (compareDates(lastUpdateMissionDate, getCurrentDate())) {
                updateNewMissions()
            } else {
                accountManager.getCurrentMissions()
            }
        }
    }

    fun updateChallengers() {
        viewModelScope.launch {
            val result = homeRepository.getChallengers()
            viewModelState.update { it.copy(isLoading = true) }

            viewModelState.update {
                when (result) {
                    is Success -> {
                        challengerList = result.data.challengers.toMutableList()
                        it.copy(challengers = result.data, isLoading = false)
                    }

                    is Error -> {
                        val errorMessages = ErrorMessage(
                            id = UUID.randomUUID().mostSignificantBits,
                            messageId = R.string.load_error
                        )
                        it.copy(errorMessages = errorMessages, isLoading = false)
                    }
                }
            }
        }
    }

    private fun observables() {
        viewModelScope.launch {
            accountManager.updateCoins()

            accountManager.observeCoins().collect { coin ->
                viewModelState.update { it.copy(coin = coin) }
            }
        }

        viewModelScope.launch {
            accountManager.observeMissions().collect { missions ->
                val missionsNotChecked = missions.filter { mission -> !mission.isChecked }.size
                val isFinishAllMissions = missionsNotChecked == 0

                if (missions.isNotEmpty()) {
                    viewModelState.update {
                        it.copy(
                            badgeCount = missionsNotChecked,
                            finishAllMissions = isFinishAllMissions
                        )
                    }
                }
            }
        }
    }

    fun completedChallenger(
        challengerSelected: Challenger.Card,
        snackbarHostState: SnackbarHostState
    ) {
        viewModelScope.launch {
            val userCoins = accountManager.getQuantityCoins()

            if (userCoins < challengerSelected.value) {
                snackbarHostState.showSnackbar(INSUFFICIENT_QUANTITY)
            } else {
                val index = challengerList.indexOf(challengerSelected)

                if (index != -1) {
                    challengerList[index] = challengerList[index].copy(complete = true)

                    viewModelState.update {
                        when (val result = homeRepository.completeChallenger(challengerList)) {
                            is Success -> {
                                val newCoins = userCoins - challengerSelected.value
                                updateCoin(newCoins)
                                it.copy(
                                    challengers = result.data,
                                    selectedChallenger = challengerSelected.copy(complete = true)
                                )
                            }

                            is Error -> {
                                snackbarHostState.showSnackbar(ERROR)
                                return@launch
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateCoin(coin: Int) {
        viewModelScope.launch {
            val user = accountManager.getUserLogged().copy(quantityCoins = coin)
            accountManager.updateUserInfo(user)
                .onSuccess {
                    viewModelState.update {
                        it.copy(coin = coin)
                    }
                }
        }
    }

    private fun updateNewMissions() {
        viewModelScope.launch {
            when (val result = homeRepository.getRandomMissions()) {
                is Success -> {

                    updateCurrentMissionsInUser(
                        missions = result.data,
                        lastUpdateCurrentMissions = getCurrentDate()
                    )
                }

                is Error -> {
                    viewModelState.update {
                        val errorMessages = ErrorMessage(
                            id = UUID.randomUUID().mostSignificantBits,
                            messageId = R.string.load_error
                        )
                        it.copy(errorMessages = errorMessages, isLoading = false)
                    }
                }
            }

        }
    }

    private suspend fun updateCurrentMissionsInUser(
        missions: List<Missions.MissionsModel>,
        lastUpdateCurrentMissions: String
    ) {
        val user = accountManager.getUserLogged().copy(
            currentMissions = missions,
            lastUpdateMissions = lastUpdateCurrentMissions
        )

        accountManager.updateUserInfo(user).onFailure { _ ->
            viewModelState.update {
                val errorMessages = ErrorMessage(
                    id = UUID.randomUUID().mostSignificantBits,
                    messageId = R.string.load_error
                )
                it.copy(errorMessages = errorMessages, isLoading = false)
            }
        }
    }


    fun challengerSelected(challengers: Challenger.Card) {
        viewModelState.update {
            it.copy(
                selectedChallenger = challengers
            )
        }
    }

    fun onInteractionFeed() {
        viewModelState.update {
            it.copy(selectedChallenger = null)
        }
    }

    fun openMissions(visible: Boolean) {
        viewModelState.update {
            it.copy(showMissions = visible)
        }
    }

    companion object {
        private const val INSUFFICIENT_QUANTITY = "Quantidade de moedas insuficientes"
        private const val ERROR = "Erro ao completar esse desafio."
    }
}