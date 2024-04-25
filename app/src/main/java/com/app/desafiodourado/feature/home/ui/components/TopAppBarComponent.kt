package com.app.desafiodourado.feature.home.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.desafiodourado.R
import com.app.desafiodourado.components.bottomsheet.BetterModalBottomSheet
import com.app.desafiodourado.components.toolbar.ToolbarCustom
import com.app.desafiodourado.feature.home.ui.HomeUiState
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.commons.mock.challengers
import com.app.desafiodourado.core.utils.ErrorMessage
import com.app.desafiodourado.feature.missions.ui.MissionsRoute
import com.app.desafiodourado.theme.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomChallengerFeedTollBar(
    navController: NavController,
    uiState: HomeUiState.HasChallengers,
    onMissionsListener: (visible: Boolean) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(uiState.showMissions) }

    ToolbarCustom(
        title = stringResource(id = R.string.challengers_feed_screen_toolbar_title),
        badgeCount = uiState.badgeCount,
        finishAllMissions = uiState.finishAllMissions,
        showNavigationIcon = false,
        showBadgeCount = true,
        onMissionsListener = {
            onMissionsListener(!uiState.showMissions)
            showBottomSheet = !uiState.showMissions
        },
        onNavigationListener = {}
    )

    BetterModalBottomSheet(
        showSheet = showBottomSheet,
        onDismissRequest = {
            showBottomSheet = false
            onMissionsListener(false)
        },
        containerColor = Background
    ) {
        MissionsRoute(navController = navController)
    }
}

@Preview
@Composable
fun CustomChallengerFeedTollBarPreview() {
    CustomChallengerFeedTollBar(
        uiState = HomeUiState.HasChallengers(
            challengers = Challenger(
                challengers
            ),
            selectedChallenger = null,
            badgeCount = 6,
            finishAllMissions = false,
            coin = 200,
            showMissions = false,
            isLoading = true,
            isRefresh = false,
            errorMessages = ErrorMessage(id = 20L, messageId = R.string.load_error),
        ),
        navController = rememberNavController(),
    ) {}
}

@Preview
@Composable
fun CustomChallengerFeedTollBarCompleteQuestionsPreview() {
    CustomChallengerFeedTollBar(
        uiState = HomeUiState.HasChallengers(
            challengers = Challenger(
                challengers
            ),
            selectedChallenger = null,
            badgeCount = 6,
            finishAllMissions = true,
            coin = 200,
            showMissions = false,
            isLoading = true,
            isRefresh = false,
            errorMessages = ErrorMessage(id = 20L, messageId = R.string.load_error),
        ),
        navController = rememberNavController(),
    ) {}
}