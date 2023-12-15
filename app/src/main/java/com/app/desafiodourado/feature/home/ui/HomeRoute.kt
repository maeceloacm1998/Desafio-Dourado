package com.app.desafiodourado.feature.home.ui

import androidx.activity.compose.BackHandler
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.desafiodourado.feature.details.ui.DetailsRoute
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.feature.home.ui.HomeScreenType.ChallengerFeed
import com.app.desafiodourado.feature.home.ui.HomeScreenType.ChallengerDetails
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    HomeRoute(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onChallengerSelected = { homeViewModel.challengerSelected(it) },
        onInteractionWithFeed = { homeViewModel.onInteractionFeed() },
        onMissionsListener = { homeViewModel.openMissions(it) },
        onRefreshChallengers = { homeViewModel.refresh() },
        onRetryChallengers = { homeViewModel.refresh() },
    )
}

@Composable
fun HomeRoute(
    uiState: HomeUiState,
    snackbarHostState: SnackbarHostState,
    onChallengerSelected: (challengerSelected: Challenger.Card) -> Unit,
    onInteractionWithFeed: () -> Unit,
    onMissionsListener: (visible: Boolean) -> Unit,
    onRefreshChallengers: () -> Unit,
    onRetryChallengers: () -> Unit
) {
    when (getHomeScreenType(uiState)) {
        ChallengerFeed -> {
            ChallengerFeed(
                uiState = uiState,
                onChallengerSelected = onChallengerSelected,
                onMissionsListener = onMissionsListener,
                onRefreshChallengers = onRefreshChallengers,
                onRetryChallengers = onRetryChallengers
            )
        }

        ChallengerDetails -> {
            check(uiState is HomeUiState.HasChallengers)
            uiState.selectedChallenger?.let {
                DetailsRoute(
                    challengerItem = it,
                    challengerList = uiState.challengers.challengers,
                    userCoins = uiState.coin,
                    onBack = onInteractionWithFeed,
                )
                BackHandler {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    onInteractionWithFeed()
                }
            }
        }
    }
}

private enum class HomeScreenType {
    ChallengerDetails,
    ChallengerFeed
}

@Composable
private fun getHomeScreenType(
    uiState: HomeUiState
): HomeScreenType = when (uiState) {
    is HomeUiState.HasChallengers -> {
        if (uiState.selectedChallenger != null) {
            ChallengerDetails
        } else {
            ChallengerFeed
        }
    }

    is HomeUiState.NoChallengers -> ChallengerFeed
}

