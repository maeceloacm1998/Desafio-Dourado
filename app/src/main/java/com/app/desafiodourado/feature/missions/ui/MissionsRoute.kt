package com.app.desafiodourado.feature.missions.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.desafiodourado.components.states.Error
import com.app.desafiodourado.feature.home.ui.model.Missions
import com.app.desafiodourado.feature.missions.ui.MissionsScreenType.MissionError
import com.app.desafiodourado.feature.missions.ui.MissionsScreenType.MissionsScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun MissionsRoute(missionsViewModel: MissionsViewModel = koinViewModel()) {
    val uiState by missionsViewModel.uiState.collectAsStateWithLifecycle()

    MissionsRoute(
        uiState = uiState,
        onCheckMission = { mission -> missionsViewModel.missionsChecked(mission) },
        onClickRetryListener = {}
    )
}

@Composable
fun MissionsRoute(
    uiState: MissionsUiState,
    onCheckMission: (mission: Missions.MissionsModel) -> Unit,
    onClickRetryListener: () -> Unit
) {
    when (getMissionsScreenType(uiState)) {
        MissionsScreen -> {
            check(uiState is MissionsUiState.HasMissions)
            MissionsScreen(
                uiState = uiState,
                onCheckMission = onCheckMission
            )
        }

        MissionError -> {
            check(uiState is MissionsUiState.NoMissions)
            Error(
                title = "Error ao carregar os desafios",
                onClickRetryListener = onClickRetryListener
            )
        }
    }
}


private enum class MissionsScreenType {
    MissionsScreen,
    MissionError
}

@Composable
private fun getMissionsScreenType(
    uiState: MissionsUiState
): Any = when (uiState) {
    is MissionsUiState.HasMissions -> {
        MissionsScreen
    }

    is MissionsUiState.NoMissions -> {
        MissionError
    }
}
