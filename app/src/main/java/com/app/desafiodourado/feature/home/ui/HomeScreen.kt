package com.app.desafiodourado.feature.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import com.app.desafiodourado.components.image.ImageComponent
import com.app.desafiodourado.components.toolbar.ToolbarCustom
import com.app.desafiodourado.core.utils.UiState
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.ui.theme.Background
import com.app.desafiodourado.ui.theme.BrowLight
import com.app.desafiodourado.ui.theme.CustomDimensions
import com.app.desafiodourado.ui.theme.PurpleLight
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = koinViewModel()) {
    val tabs = listOf("Desafios Pendentes", "Desafios Concluidos")

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<UiState<List<Challenger.Card>>>(
        initialValue = UiState.Loading, key1 = lifecycle, key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    LaunchedEffect(key1 = Lifecycle.State.STARTED) {
        viewModel.getChallengers()
    }

    when (uiState) {
        is UiState.Loading -> {

        }

        is UiState.Success -> {
            val response = (uiState as UiState.Success).response

            HomeComponents(
                tabs = tabs,
                challenger = response,
                coins = viewModel.getCoins(),
                badgeCount = 2,
                onClickTabOptionListener = { index ->
                    viewModel.updateChallengerList(index)
                }
            )
        }

        is UiState.Error -> {

        }
    }
}

@Composable
fun HomeComponents(
    tabs: List<String>,
    coins: Int,
    badgeCount: Int,
    challenger: List<Challenger.Card>,
    onClickTabOptionListener: (position: Int) -> Unit
) {
    Background {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBar(coin = coins)
            ToolbarCustom(title = "Premios Misteriosos", badgeCount = badgeCount) {

            }
            TopTabRow(tabs = tabs) { index ->
                onClickTabOptionListener(index)
            }
            ChallengerList(challenger)
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
                    horizontal = CustomDimensions.padding20,
                    vertical = CustomDimensions.padding14
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
                    horizontal = CustomDimensions.padding20,
                    vertical = CustomDimensions.padding14
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
        TabRow(
            selectedTabIndex = state,
            containerColor = Background,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[state]),
                    height = CustomDimensions.padding2,
                    color = BrowLight
                )
            }
        ) {
            tabs.forEachIndexed { position, title ->
                Tab(
                    selected = state == position,
                    onClick = {
                        state = position
                        onClickTabOptionListener(position)
                    },
                    text = {
                        Text(
                            text = title,
                            color = if (position != state) PurpleLight else BrowLight,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun ChallengerList(challengerList: List<Challenger.Card>) {
    val state = rememberLazyGridState()

    LazyVerticalGrid(state = state, columns = GridCells.Fixed(count = 3)) {
        items(challengerList) { item ->
            ImageComponent(
                modifier = Modifier
                    .size(
                        width = CustomDimensions.padding160,
                        height = CustomDimensions.padding160
                    )
                    .padding(vertical = CustomDimensions.padding2),
                url = item.image
            )
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
fun HomePreview() {
    val titles = listOf("Desafios Pendentes", "Desafios Concluidos")
    HomeComponents(
        tabs = titles,
        challenger = listOf(),
        coins = 200,
        badgeCount = 2,
        onClickTabOptionListener = {}
    )
}

@Preview
@Composable
fun TopBarPreview() {
    TopBar(coin = 4000)
}