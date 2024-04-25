package com.app.desafiodourado.feature.missions.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.app.desafiodourado.components.states.Error
import com.app.desafiodourado.core.routes.Routes
import com.app.desafiodourado.feature.home.ui.model.Missions
import com.app.desafiodourado.feature.missions.ui.MissionsScreenType.MissionError
import com.app.desafiodourado.feature.missions.ui.MissionsScreenType.MissionsScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun MissionsRoute(
    navController: NavController,
    missionsViewModel: MissionsViewModel = koinViewModel()
) {
    val uiState by missionsViewModel.uiState.collectAsStateWithLifecycle()

    MissionsRoute(
        uiState = uiState,
        onClickFeedback = { navController.navigate(Routes.Feedback.route) },
        onCheckMission = { mission -> missionsViewModel.missionsChecked(mission) },
        onClickRetryListener = { navController.navigate(Routes.Feedback.route) }
    )
}

@Composable
fun MissionsRoute(
    uiState: MissionsUiState,
    onClickFeedback: () -> Unit,
    onCheckMission: (mission: Missions.MissionsModel) -> Unit,
    onClickRetryListener: () -> Unit
) {
    when (getMissionsScreenType(uiState)) {
        MissionsScreen -> {
            check(uiState is MissionsUiState.HasMissions)
            MissionsScreen(
                uiState = uiState,
                onClickFeedback = onClickFeedback,
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
