package com.app.desafiodourado.feature.home.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.app.desafiodourado.R
import com.app.desafiodourado.components.bottomsheet.BetterModalBottomSheet
import com.app.desafiodourado.components.toolbar.ToolbarCustom
import com.app.desafiodourado.feature.home.ui.HomeUiState
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.feature.initial.data.challengers
import com.app.desafiodourado.feature.missions.ui.MissionsRoute
import com.app.desafiodourado.theme.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomChallengerFeedTollBar(
    uiState: HomeUiState.HasChallengers,
    onMissionsListener: (visible: Boolean) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(uiState.showMissions) }

    ToolbarCustom(
        title = stringResource(id = R.string.challengers_feed_screen_toolbar_title),
        badgeCount = uiState.badgeCount,
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
        MissionsRoute()
    }
}

@Preview
@Composable
fun MissionBottomSheet() {
    CustomChallengerFeedTollBar(
        uiState = HomeUiState.HasChallengers(
            challengers = Challenger(
                challengers
            ),
            showMissions = true,
            coin = 2000,
            badgeCount = 2,
            selectedChallenger = challengers[0],
            isLoading = false,
            errorMessages = null
        ),
        onMissionsListener = {}
    )
}

