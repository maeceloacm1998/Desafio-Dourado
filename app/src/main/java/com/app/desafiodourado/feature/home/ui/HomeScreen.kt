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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.app.desafiodourado.components.background.Background
import com.app.desafiodourado.components.coinview.CoinView
import com.app.desafiodourado.components.dialog.CustomDialog
import com.app.desafiodourado.components.states.Error
import com.app.desafiodourado.components.states.Loading
import com.app.desafiodourado.components.toolbar.ToolbarCustom
import com.app.desafiodourado.core.utils.UiState
import com.app.desafiodourado.feature.home.ui.components.ChallengerList
import com.app.desafiodourado.feature.home.ui.components.DialogDetails
import com.app.desafiodourado.feature.home.ui.components.InfoComponent
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.feature.home.ui.model.Challenger.Card
import com.app.desafiodourado.ui.theme.Background
import com.app.desafiodourado.ui.theme.BrowLight
import com.app.desafiodourado.ui.theme.CustomDimensions
import com.app.desafiodourado.ui.theme.PurpleLight
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    val tabs = listOf("Desafios Pendentes", "Desafios Concluidos")
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<UiState<List<Card>>>(
        initialValue = UiState.Loading, key1 = lifecycle, key2 = viewModel
    ) {
        viewModel.getChallengers()

        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.challengerState.collect { value = it }
        }
    }

    HomeComponents(
        challengerState = uiState,
        tabs = tabs,
        coins = viewModel.getCoins(),
        onClickTabOptionListener = { index ->
            viewModel.updateChallengerList(index)
        },
        onClickRetryListener = { viewModel.getChallengers() },
        onUpdateChallengerList = { challenger ->

        }
    )
}

@Composable
fun HomeComponents(
    challengerState: UiState<List<Card>>,
    tabs: List<String>,
    coins: Int,
    onClickTabOptionListener: (position: Int) -> Unit,
    onClickRetryListener: () -> Unit,
    onUpdateChallengerList: (challenger: Challenger.Card) -> Unit
) {
    Background {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            var dialogState by rememberSaveable { mutableStateOf(false) }
            var challengerSelected by remember { mutableStateOf(Card()) }

            when (challengerState) {
                is UiState.Loading -> Loading()

                is UiState.Success -> {
                    val response = challengerState.response
                    TopBar(coin = coins)
                    ToolbarCustom(title = "Premios Misteriosos", badgeCount = 2) {

                    }
                    TopTabRow(tabs = tabs) { index ->
                        onClickTabOptionListener(index)
                    }
                    InfoComponent()
                    ChallengerList(response) { challenger ->
                        dialogState = true
                        challengerSelected = challenger
                    }
                    CustomDialog(
                        showDialog = dialogState,
                        onDismissDialog = { dialogState = false }
                    ) {
                        DialogDetails(
                            challenger = challengerSelected,
                            onClickCancelListener = { dialogState = false },
                            onClickSubmitListener = { challenger ->

                            }
                        )
                    }
                }

                is UiState.Error -> {
                    Error(
                        title = "Ops, ocorreu um problema ao carregar os desafios.",
                        onClickRetryListener = onClickRetryListener
                    )
                }
            }
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
        tabs = titles,
        coins = 200,
        onClickTabOptionListener = {},
        onUpdateChallengerList = {},
        onClickRetryListener = {}
    )
}

@Preview
@Composable
fun HomePreviewError() {
    val titles = listOf("Desafios Pendentes", "Desafios Concluidos")
    HomeComponents(
        challengerState = UiState.Error(Throwable()),
        tabs = titles,
        coins = 200,
        onClickTabOptionListener = {},
        onUpdateChallengerList = {},
        onClickRetryListener = {},
    )
}

@Preview
@Composable
fun TopBarPreview() {
    TopBar(coin = 4000)
}