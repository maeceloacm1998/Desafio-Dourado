package com.app.desafiodourado.feature.missions.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.desafiodourado.core.accountmanager.AccountManager
import com.app.desafiodourado.feature.home.ui.model.Missions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MissionsViewModel(private val accountManager: AccountManager) : ViewModel() {
    private val viewModelState = MutableStateFlow(MissionsViewModelState(isLoading = true))

    val uiState = viewModelState
        .map(MissionsViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        observables()
    }

    private fun observables() {
        viewModelScope.launch {
            accountManager.observeMissions().collect { missions ->
                if (missions.isNotEmpty()) {
                    viewModelState.update { it.copy(missions = missions) }
                }
            }
        }

        viewModelScope.launch {
            accountManager.observeCountdown().collect { timer ->
                viewModelState.update { it.copy(timer = timer) }
            }
        }
    }

    fun missionsChecked(missionChecked: Missions.MissionsModel) {
        viewModelScope.launch {
            val user = accountManager.getUserLogged()

            val userCoin = user.quantityCoins
            val currentMissions = user.currentMissions.toMutableList()

            if (existMission(currentMissions, missionChecked)) {
                val missionIndex = currentMissions.indexOf(missionChecked)
                val mission = currentMissions[missionIndex]
                val newUserCoin = userCoin + missionChecked.coinValue

                currentMissions[missionIndex] = mission.copy(isChecked = !missionChecked.isChecked)
                updateUserMissions(missions = currentMissions, quantityCoins = newUserCoin)
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

    private fun existMission(
        missions: List<Missions.MissionsModel>,
        mission: Missions.MissionsModel
    ): Boolean = missions.contains(mission)
}