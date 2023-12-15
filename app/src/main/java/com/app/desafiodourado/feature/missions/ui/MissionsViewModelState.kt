package com.app.desafiodourado.feature.missions.ui

import com.app.desafiodourado.core.utils.ErrorMessage
import com.app.desafiodourado.feature.home.ui.model.Missions


sealed interface MissionsUiState {
    val errorMessages: ErrorMessage?

    data class NoMissions(
        override val errorMessages: ErrorMessage?,
    ) : MissionsUiState

    data class HasMissions(
        val missions: List<Missions.MissionsModel>,
        val selectedMissions: Missions.MissionsModel?,
        val timer: String?,
        override val errorMessages: ErrorMessage?,
    ): MissionsUiState
}

data class MissionsViewModelState(
    val missions: List<Missions.MissionsModel>? = null,
    val selectedMissions: Missions.MissionsModel? = null,
    val timer: String? = null,
    val errorMessages: ErrorMessage? = null,
) {
    fun toUiState(): MissionsUiState =
        if (missions == null) {
            MissionsUiState.NoMissions(
                errorMessages = errorMessages,
            )
        } else {
            MissionsUiState.HasMissions(
                missions = missions,
                selectedMissions = selectedMissions,
                timer = timer,
                errorMessages = errorMessages,
            )
        }
}