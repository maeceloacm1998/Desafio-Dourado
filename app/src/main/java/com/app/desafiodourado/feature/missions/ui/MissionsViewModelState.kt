package com.app.desafiodourado.feature.missions.ui

import com.app.desafiodourado.core.utils.ErrorMessage
import com.app.desafiodourado.feature.home.ui.model.Missions


sealed interface MissionsUiState {
    val isLoading: Boolean
    val errorMessages: ErrorMessage?

    data class NoMissions(
        override val isLoading: Boolean,
        override val errorMessages: ErrorMessage?,
    ) : MissionsUiState

    data class HasMissions(
        val missions: List<Missions.MissionsModel>,
        val selectedMissions: Missions.MissionsModel?,
        val timer: String?,
        override val isLoading: Boolean,
        override val errorMessages: ErrorMessage?,
    ): MissionsUiState
}

data class MissionsViewModelState(
    val missions: List<Missions.MissionsModel>? = null,
    val selectedMissions: Missions.MissionsModel? = null,
    val timer: String? = null,
    val isLoading: Boolean = false,
    val errorMessages: ErrorMessage? = null,
) {
    fun toUiState(): MissionsUiState =
        if (missions == null) {
            MissionsUiState.NoMissions(
                isLoading = isLoading,
                errorMessages = errorMessages,
            )
        } else {
            MissionsUiState.HasMissions(
                missions = missions,
                selectedMissions = selectedMissions,
                timer = timer,
                isLoading = isLoading,
                errorMessages = errorMessages,
            )
        }
}