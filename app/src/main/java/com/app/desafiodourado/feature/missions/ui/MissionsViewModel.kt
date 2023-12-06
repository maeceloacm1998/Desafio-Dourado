package com.app.desafiodourado.feature.missions.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.desafiodourado.R
import com.app.desafiodourado.core.accountManager.AccountManager
import com.app.desafiodourado.core.utils.ErrorMessage
import com.app.desafiodourado.core.utils.Result
import com.app.desafiodourado.feature.home.ui.model.Missions
import com.app.desafiodourado.feature.missions.data.MissionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

const val EMPTY_SEARCH = -1

class MissionsViewModel(
    private val missionsRepository: MissionsRepository,
    private val accountManager: AccountManager
) : ViewModel() {
    private val viewModelState = MutableStateFlow(MissionsViewModelState(isLoading = true))

    val uiState = viewModelState
        .map(MissionsViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        viewModelScope.launch {
            accountManager.observeMissions().collect { missions ->
                if (missions.isNotEmpty()) {
                    viewModelState.update { it.copy(missions = missions) }
                }
            }
        }
    }

    fun missionsChecked(mission: Missions.MissionsModel) {
        viewModelScope.launch {
            val missions = accountManager.getUserLogged().currentMissions
            val getMissions = missions.toMutableList()
            val index = getMissionsIndex(missions = missions, mission = mission)
            if (index != EMPTY_SEARCH) {
                val missionsPosition = missions[index]
                val quantityCoin = missionsPosition.coinValue + mission.coinValue
                getMissions[index] = missionsPosition.copy(
                    coinValue = quantityCoin,
                    isChecked = !mission.isChecked
                )
                updateUserMissions(missions = getMissions, quantityCoins = quantityCoin)
            }
        }
    }

    private suspend fun updateUserMissions(
        missions: List<Missions.MissionsModel>,
        quantityCoins: Int
    ) {
        val user = accountManager.getUserLogged()
        accountManager.updateUserInfo(
            user.copy(
                currentMissions = missions,
                quantityCoins = quantityCoins
            )
        )
    }

    private fun getMissionsIndex(
        missions: List<Missions.MissionsModel>,
        mission: Missions.MissionsModel
    ): Int = missions.indexOf(mission)

    private fun updateMissions() {
        viewModelScope.launch {
            viewModelState.update {
                when (val result = missionsRepository.getRandomMissions()) {
                    is Result.Success -> {
                        updateCurrentMissionsInUser(missions = result.data)
                        it.copy(missions = result.data)
                    }

                    is Result.Error -> {
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

    private suspend fun updateCurrentMissionsInUser(missions: List<Missions.MissionsModel>) {
        val user = accountManager.getUserLogged().copy(currentMissions = missions)

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

}