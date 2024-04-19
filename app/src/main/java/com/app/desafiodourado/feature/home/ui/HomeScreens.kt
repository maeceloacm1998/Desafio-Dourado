package com.app.desafiodourado.feature.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.app.desafiodourado.core.utils.ErrorMessage
import com.app.desafiodourado.commons.mock.challengers
import com.app.desafiodourado.feature.home.ui.components.ChallengerList
import com.app.desafiodourado.feature.home.ui.components.CustomChallengerFeedTollBar
import com.app.desafiodourado.feature.home.ui.components.InfoComponent
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.theme.CustomDimensions
import com.app.desafiodourado.theme.DesafioDouradoTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun ChallengerFeed(
    uiState: HomeUiState,
    onChallengerSelected: (challengerSelected: Challenger.Card) -> Unit,
    onMissionsListener: (visible: Boolean) -> Unit,
    onRefreshChallengers: () -> Unit,
    onRetryChallengers: () -> Unit
) {
    HomeScreenWithList(
        uiState = uiState,
        onRefreshChallengers = onRefreshChallengers,
        onRetryChallengers = onRetryChallengers
    ) { state ->
        Column {
            TopBar(coin = state.coin)
            CustomChallengerFeedTollBar(
                uiState = state,
                onMissionsListener = onMissionsListener
            )
            InfoComponent()
            ChallengerList(state) { challengerSelected ->
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
        isLoading = uiState.isLoading,
        isRefresh = uiState.isRefresh,
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
    isLoading: Boolean,
    isRefresh: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {

    when {
        isLoading -> Loading()
        else -> SwipeRefresh(
            state = rememberSwipeRefreshState(isRefresh),
            onRefresh = onRefresh,
            content = content
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
            text = stringResource(id = R.string.challengers_feed_screen_version_title),
            style = MaterialTheme.typography.titleSmall,
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

@Preview(showBackground = true)
@Composable
fun PreviewChallengerFeedScreen() {
    DesafioDouradoTheme {
        Background {
            ChallengerFeed(
                uiState = HomeUiState.HasChallengers(
                    challengers = Challenger(
                        challengers
                    ),
                    selectedChallenger = null,
                    badgeCount = 0,
                    finishAllMissions = false,
                    coin = 200,
                    showMissions = false,
                    isLoading = false,
                    isRefresh = false,
                    errorMessages = ErrorMessage(
                        id = 20L,
                        messageId = R.string.load_error
                    ),
                ),
                onChallengerSelected = {},
                onMissionsListener = {},
                onRefreshChallengers = {},
                onRetryChallengers = {}
            )
        }
    }
}

@Preview
@Composable
fun PreviewChallengerFeedLoadingScreen() {
    DesafioDouradoTheme {
        Background {
            ChallengerFeed(
                uiState = HomeUiState.HasChallengers(
                    challengers = Challenger(
                        challengers
                    ),
                    selectedChallenger = null,
                    badgeCount = 0,
                    finishAllMissions = false,
                    coin = 200,
                    showMissions = false,
                    isLoading = true,
                    isRefresh = false,
                    errorMessages = ErrorMessage(id = 20L, messageId = R.string.load_error),
                ),
                onChallengerSelected = {},
                onMissionsListener = {},
                onRefreshChallengers = {},
                onRetryChallengers = {}
            )
        }
    }
}

@Preview
@Composable
fun PreviewChallengerFeedErrorScreen() {
    DesafioDouradoTheme {
        Background {
            ChallengerFeed(
                uiState = HomeUiState.NoChallengers(
                    isLoading = false,
                    isRefresh = false,
                    errorMessages = ErrorMessage(id = 20L, messageId = R.string.load_error),
                ),
                onChallengerSelected = {},
                onMissionsListener = {},
                onRefreshChallengers = {},
                onRetryChallengers = {}
            )
        }
    }
}

@Preview
@Composable
fun PreviewChallengerFeedEmptyScreen() {
    DesafioDouradoTheme {
        Background {
            ChallengerFeed(
                uiState = HomeUiState.HasChallengers(
                    challengers = Challenger(mutableListOf()),
                    selectedChallenger = null,
                    badgeCount = 0,
                    finishAllMissions = false,
                    coin = 200,
                    showMissions = false,
                    isLoading = false,
                    isRefresh = false,
                    errorMessages = ErrorMessage(id = 20L, messageId = R.string.load_error),
                ),
                onChallengerSelected = {},
                onMissionsListener = {},
                onRefreshChallengers = {},
                onRetryChallengers = {}
            )
        }
    }
}