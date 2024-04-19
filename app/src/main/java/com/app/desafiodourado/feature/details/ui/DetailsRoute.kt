package com.app.desafiodourado.feature.details.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.desafiodourado.feature.home.ui.model.Challenger
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsRoute(
    challengerList: List<Challenger.Card>,
    challengerItem: Challenger.Card,
    userCoins: Int,
    onBack: () -> Unit,
    detailsViewModel: DetailsViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val uiState by detailsViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = challengerItem) {
        detailsViewModel.init(
            challengerList = challengerList,
            challengerSelected = challengerItem,
            userCoins = userCoins
        )
    }

    DetailsRoute(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onClickSubmitListener = { challengerSelected ->
            detailsViewModel.completeChallenger(
                challengerList = challengerList,
                challengerSelected = challengerSelected,
                userCoins = userCoins,
                snackbarHostState = snackbarHostState
            )
        },
        onBack = {
            snackbarHostState.currentSnackbarData?.dismiss()
            onBack()
        }
    )
}

@Composable
fun DetailsRoute(
    uiState: DetailsUiState,
    snackbarHostState: SnackbarHostState,
    onClickSubmitListener: (challengerSelected: Challenger.Card) -> Unit,
    onBack: () -> Unit
) {
    check(uiState is DetailsUiState.HasChallengersDetails)
    DetailsScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onClickSubmitListener = onClickSubmitListener,
        onBack = onBack
    )
}
