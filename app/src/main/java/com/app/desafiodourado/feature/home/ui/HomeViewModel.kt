package com.app.desafiodourado.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.desafiodourado.R
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
    private val homeRepository: HomeRepository
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
        updateChallengers()
    }

    fun updateCoin() {
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

    fun completedChallenger(challengerSelected: Challenger.Card) {
        viewModelScope.launch {
            val index = challengerList.indexOf(challengerSelected)
            challengerList[index] = challengerList[index].copy(complete = true)
            val result = homeRepository.completeChallenger(challengerList)

            viewModelState.update {
                when (result) {
                    is Success -> {
                        it.copy(
                            challengers = result.data,
                            selectedChallenger = challengerSelected.copy(complete = true),
                            isLoading = false
                        )
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
}