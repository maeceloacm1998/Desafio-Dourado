package com.app.desafiodourado.feature.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.app.desafiodourado.components.background.Background
import com.app.desafiodourado.components.coinview.CoinView
import com.app.desafiodourado.components.states.Error
import com.app.desafiodourado.components.states.Loading
import com.app.desafiodourado.components.toolbar.ToolbarCustom
import com.app.desafiodourado.core.utils.UiState
import com.app.desafiodourado.feature.home.ui.components.ChallengerList
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.ui.theme.Background
import com.app.desafiodourado.ui.theme.BrowLight
import com.app.desafiodourado.ui.theme.CustomDimensions
import com.app.desafiodourado.ui.theme.PurpleLight
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    val tabs = listOf("Desafios Pendentes", "Desafios Concluidos")

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val challengerState by produceState<UiState<List<Challenger.Card>>>(
        initialValue = UiState.Loading, key1 = lifecycle, key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.challengerState.collect { value = it }
        }
    }
    val headerState by produceState<UiState<Int>>(
        initialValue = UiState.Loading, key1 = lifecycle, key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.headerState.collect { value = it }
        }
    }

    LaunchedEffect(key1 = Lifecycle.State.STARTED) {
        viewModel.run {
            getChallengers()
            getHeaderInfo()
        }
    }

    HomeComponents(headerState = headerState,
        challengerState = challengerState,
        tabs = tabs,
        coins = viewModel.getCoins(),
        onClickTabOptionListener = { index ->
            viewModel.updateChallengerList(index)
        },
        onClickRetryHeaderListener = {
            viewModel.getHeaderInfo()
        },
        onClickRetryChallengerListener = {
            viewModel.getChallengers()
        })
}

@Composable
fun HomeComponents(
    headerState: UiState<Int>,
    challengerState: UiState<List<Challenger.Card>>,
    tabs: List<String>,
    coins: Int,
    onClickTabOptionListener: (position: Int) -> Unit,
    onClickRetryChallengerListener: () -> Unit,
    onClickRetryHeaderListener: () -> Unit
) {
    Background {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HeaderObservable(
                headerState = headerState,
                tabs = tabs,
                coins = coins,
                onClickTabOptionListener = onClickTabOptionListener,
                onClickRetryListener = onClickRetryChallengerListener
            )
            ChallengerListObservable(
                challengerState = challengerState, onClickRetryListener = onClickRetryHeaderListener
            )
        }
    }
}

@Composable
fun HeaderObservable(
    headerState: UiState<Int>,
    tabs: List<String>,
    coins: Int,
    onClickTabOptionListener: (position: Int) -> Unit,
    onClickRetryListener: () -> Unit
) {
    when (headerState) {
        is UiState.Loading -> Loading()

        is UiState.Success -> {
            val badgeCount = headerState.response
            TopBar(coin = coins)
            ToolbarCustom(title = "Premios Misteriosos", badgeCount = badgeCount) {

            }
            TopTabRow(tabs = tabs) { index ->
                onClickTabOptionListener(index)
            }
        }

        is UiState.Error -> {
            Error(
                title = "Ops, ocorreu um problema ao carregar os dados.",
                onClickRetryListener = onClickRetryListener
            )
        }
    }
}

@Composable
fun ChallengerListObservable(
    challengerState: UiState<List<Challenger.Card>>, onClickRetryListener: () -> Unit
) {
    when (challengerState) {
        is UiState.Loading -> Unit

        is UiState.Success -> {
            val response = challengerState.response
            ChallengerList(response)
        }

        is UiState.Error -> {
            Error(
                title = "Ops, ocorreu um problema ao carregar os desafios.",
                onClickRetryListener = onClickRetryListener
            )
        }
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

@Composable
fun TopTabRow(tabs: List<String>, onClickTabOptionListener: (position: Int) -> Unit) {
    var state by remember { mutableIntStateOf(0) }

    ConstraintLayout(modifier = Modifier.padding(top = CustomDimensions.padding20)) {
        TabRow(selectedTabIndex = state, containerColor = Background, indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(tabPositions[state]),
                height = CustomDimensions.padding2,
                color = BrowLight
            )
        }) {
            tabs.forEachIndexed { position, title ->
                Tab(selected = state == position, onClick = {
                    state = position
                    onClickTabOptionListener(position)
                }, text = {
                    Text(
                        text = title,
                        color = if (position != state) PurpleLight else BrowLight,
                        style = MaterialTheme.typography.titleMedium
                    )
                })
            }
        }
    }
}

@Preview
@Composable
fun TopTabRowPreview() {
    val titles = listOf("Desafios Pendentes", "Desafios Concluidos")
    TopTabRow(tabs = titles) {}
}

@Preview
@Composable
fun HomePreviewLoading() {
    val titles = listOf("Desafios Pendentes", "Desafios Concluidos")
    HomeComponents(
        challengerState = UiState.Loading,
        headerState = UiState.Loading,
        tabs = titles,
        coins = 200,
        onClickTabOptionListener = {},
        onClickRetryChallengerListener = {},
        onClickRetryHeaderListener = {}
    )
}

@Preview
@Composable
fun HomePreviewError() {
    val titles = listOf("Desafios Pendentes", "Desafios Concluidos")
    HomeComponents(
        challengerState = UiState.Error(Throwable()),
        headerState = UiState.Error(Throwable()),
        tabs = titles,
        coins = 200,
        onClickTabOptionListener = {},
        onClickRetryChallengerListener = {},
        onClickRetryHeaderListener = {})
}

@Preview
@Composable
fun TopBarPreview() {
    TopBar(coin = 4000)
}