package com.app.desafiodourado.feature.home.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.desafiodourado.R
import com.app.desafiodourado.components.bottomsheet.BetterModalBottomSheet
import com.app.desafiodourado.components.toolbar.ToolbarCustom
import com.app.desafiodourado.feature.home.ui.HomeUiState
import com.app.desafiodourado.ui.theme.Background


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomChallengerFeedTollBar(
    uiState: HomeUiState.HasChallengers,
    onMissionsListener: () -> Unit
) {
    var showBottomSheet by rememberSaveable { mutableStateOf(uiState.showMissions) }

    ToolbarCustom(
        title = stringResource(id = R.string.challengers_feed_screen_toolbar_title),
        badgeCount = uiState.badgeCount,
        showNavigationIcon = false,
        showBadgeCount = true,
        onChallengerListener = {},
        onNavigationListener = {}
    )

    BetterModalBottomSheet(
        showSheet = showBottomSheet,
        onDismissRequest = {
            showBottomSheet = false
        },
        containerColor = Background
    ) {
        InfoBottomSheet()
    }
}

@Preview
@Composable
fun MissionBottomSheet() {
    ConstraintLayout {

    }
}

