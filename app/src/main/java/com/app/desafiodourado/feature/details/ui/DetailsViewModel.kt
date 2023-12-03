package com.app.desafiodourado.feature.details.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.desafiodourado.core.utils.UiState
import com.app.desafiodourado.feature.details.domain.UpdateChallengersUseCase
import com.app.desafiodourado.feature.home.domain.GetChallengersUseCase
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.feature.home.ui.model.Challenger.Card
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val getChallengersUseCase: GetChallengersUseCase,
    private val updateChallengersUseCase: UpdateChallengersUseCase
) : ViewModel() {

    private var challenger: Card = Card()
    private var challengerList: List<Card> = mutableListOf()

    private val _challengerState = MutableLiveData<UiState<Card>>(UiState.Loading)
    val challengerState: LiveData<UiState<Card>> = _challengerState

    private val _updateChallenger = MutableLiveData<Boolean>()
    val updateChallenger: LiveData<Boolean> = _updateChallenger

    fun getChallengers(challengerId: String) {
        viewModelScope.launch {
            getChallengersUseCase().onSuccess { document ->
                val challenger = checkNotNull(document.toObject(Challenger::class.java))
                challengerList = challenger.challengers
                val challengerCard = challenger.challengers.first { it.id == challengerId }
                _challengerState.value = UiState.Success(challengerCard)
            }.onFailure {
                _challengerState.value = UiState.Error(it)
            }
        }
    }

    fun updateChallenger(challenger: Card) {
        viewModelScope.launch {
            updateChallengersUseCase(
                challengerList = challengerList,
                challenger = challenger
            ).onSuccess {

            }.onFailure {

            }
        }
    }
}