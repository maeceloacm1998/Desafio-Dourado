package com.app.desafiodourado.feature.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.desafiodourado.R
import com.app.desafiodourado.components.background.Background
import com.app.desafiodourado.components.coinview.CoinView
import com.app.desafiodourado.components.states.Error
import com.app.desafiodourado.components.states.Loading
import com.app.desafiodourado.components.toolbar.ToolbarCustom
import com.app.desafiodourado.core.utils.ErrorMessage
import com.app.desafiodourado.core.utils.Result
import com.app.desafiodourado.feature.home.data.FakeHomeRepositoryImpl
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.ui.theme.Background
import com.app.desafiodourado.ui.theme.BrowLight
import com.app.desafiodourado.ui.theme.CustomDimensions
import com.app.desafiodourado.ui.theme.DesafioDouradoTheme
import com.app.desafiodourado.ui.theme.PurpleLight
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.runBlocking

@Composable
fun ChallengerFeed(
    uiState: HomeUiState,
    onChallengerSelected: (challengerSelected: Challenger.Card) -> Unit,
    onRefreshChallengers: () -> Unit,
    onRetryChallengers: () -> Unit
) {
    HomeScreenWithList(
        uiState = uiState,
        onRefreshChallengers = onRefreshChallengers,
        onRetryChallengers = onRetryChallengers
    ) { uiState ->
        Column {
            TopBar(coin = uiState.coin)
            ToolbarCustom(
                title = "Premios Misteriosos",
                badgeCount = 2,
                showNavigationIcon = false,
                showBadgeCount = true,
                onChallengerListener = {},
                onNavigationListener = {}
            )
            InfoComponent()
            ChallengerList(uiState.challengers.challengers) { challengerSelected ->
                onChallengerSelected(challengerSelected)
            }
        }
    }
}

@Composable
private fun HomeScreenWithList(
    uiState: HomeUiState,
    onRefreshChallengers: () -> Unit,
    onRetryChallengers: () -> Unit,
    hasChallengersContent: @Composable (
        uiState: HomeUiState.HasChallengers
    ) -> Unit
) {
    LoadingContent(
        empty = when (uiState) {
            is HomeUiState.HasChallengers -> false
            is HomeUiState.NoChallengers -> uiState.isLoading
        },
        emptyContent = { Loading() },
        loading = uiState.isLoading,
        onRefresh = onRefreshChallengers,
        content = {
            when (uiState) {
                is HomeUiState.HasChallengers -> hasChallengersContent(uiState)

                is HomeUiState.NoChallengers -> {
                    val errorMessage = uiState.errorMessages?.messageId
                    if (errorMessage != null)
                        Error(
                            title = stringResource(id = errorMessage),
                            onClickRetryListener = onRetryChallengers
                        )
                }
            }
        }
    )
}

@Composable
private fun LoadingContent(
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    loading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        SwipeRefresh(
            state = rememberSwipeRefreshState(loading),
            onRefresh = onRefresh,
            content = content,
        )
    }
}

@Composable
fun TopBar(coin: Int) {
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val (txtVersion, coinView) = createRefs()
        createHorizontalChain(txtVersion, coinView, chainStyle = ChainStyle.SpreadInside)
        Text(
            modifier = Modifier
                .padding(
                    horizontal = CustomDimensions.padding20, vertical = CustomDimensions.padding14
                )
                .constrainAs(txtVersion) {
                    end.linkTo(coinView.start)
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
            text = "v1.0.0",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )

        CoinView(
            modifier = Modifier
                .padding(
                    horizontal = CustomDimensions.padding20, vertical = CustomDimensions.padding14
                )
                .constrainAs(coinView) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    start.linkTo(txtVersion.end)
                },
            coin = coin
        )
    }
}

@Preview("Home challenger screen")
@Preview("Home list drawer screen (big font)", fontScale = 1.5f)
@Composable
fun PreviewChallengerFeedScreen() {
    val challengerFeed = runBlocking {
        (FakeHomeRepositoryImpl().getChallengers() as Result.Success).data
    }
    DesafioDouradoTheme {
        Background {
            ChallengerFeed(
                uiState = HomeUiState.HasChallengers(
                    challengers = challengerFeed,
                    selectedChallenger = null,
                    badgeCount = 0,
                    coin = 200,
                    isLoading = false,
                    errorMessages = ErrorMessage(id = 20L, messageId = R.string.load_error),
                ),
                onChallengerSelected = {},
                onRefreshChallengers = {},
                onRetryChallengers = {}
            )
        }
    }
}
