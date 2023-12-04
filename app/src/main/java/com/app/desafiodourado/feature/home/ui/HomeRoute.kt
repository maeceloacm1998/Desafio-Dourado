package com.app.desafiodourado.feature.home.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.desafiodourado.feature.challengerdetails.DetailsScreen
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.feature.home.ui.HomeScreenType.ChallengerFeed
import com.app.desafiodourado.feature.home.ui.HomeScreenType.ChallengerDetails
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel = koinViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    HomeRoute(
        uiState = uiState,
        onChallengerSelected = { homeViewModel.challengerSelected(it) },
        onInteractionWithFeed = { homeViewModel.onInteractionFeed() },
        onClickSubmitListener = { homeViewModel.completedChallenger(it) },
        onRefreshChallengers = { homeViewModel.updateChallengers() },
        onRetryChallengers = { homeViewModel.updateChallengers() },
    )
}

@Composable
fun HomeRoute(
    uiState: HomeUiState,
    onChallengerSelected: (challengerSelected: Challenger.Card) -> Unit,
    onInteractionWithFeed: () -> Unit,
    onClickSubmitListener: (challengerSelected: Challenger.Card) -> Unit,
    onRefreshChallengers: () -> Unit,
    onRetryChallengers: () -> Unit
) {
    when (getHomeScreenType(uiState)) {
        ChallengerFeed -> {
            ChallengerFeed(
                uiState = uiState,
                onChallengerSelected = onChallengerSelected,
                onRefreshChallengers = onRefreshChallengers,
                onRetryChallengers = onRetryChallengers
            )
        }

        ChallengerDetails -> {
            check(uiState is HomeUiState.HasChallengers)
            uiState.selectedChallenger?.let {
                DetailsScreen(
                    challengerItem = it,
                    onBack = onInteractionWithFeed,
                    onClickSubmitListener = onClickSubmitListener
                )
                BackHandler {
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

