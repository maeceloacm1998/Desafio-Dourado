package com.app.desafiodourado.feature.home.ui

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.desafiodourado.R
import com.app.desafiodourado.core.accountManager.AccountManager
import com.app.desafiodourado.core.utils.ErrorMessage
import com.app.desafiodourado.core.utils.Result.Success
import com.app.desafiodourado.core.utils.Result.Error
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.feature.home.data.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val accountManager: AccountManager,
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
        accountManager.updateCoins()
        updateChallengers()
        observables()
    }

    fun updateChallengers() {
        viewModelScope.launch {
            accountManager.updateMissions()

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