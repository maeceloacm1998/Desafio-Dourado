package com.app.desafiodourado.feature.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.app.desafiodourado.components.states.Error
import com.app.desafiodourado.components.states.Loading
import com.app.desafiodourado.components.toolbar.ToolbarCustom
import com.app.desafiodourado.core.routes.Routes
import com.app.desafiodourado.core.utils.UiState
import com.app.desafiodourado.feature.home.ui.components.ChallengerList
import com.app.desafiodourado.feature.home.ui.components.InfoComponent
import com.app.desafiodourado.feature.home.ui.model.Challenger
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreenV2(navController: NavController, viewModel: HomeViewModel = koinViewModel()) {
    val tabs = listOf("Desafios Pendentes", "Desafios Concluidos")
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by viewModel.challengerState.observeAsState()

    LaunchedEffect(key1 = Lifecycle.State.STARTED) {
        viewModel.getChallengers()
    }

    LaunchedEffect(key1 = Lifecycle.State.RESUMED) {
        viewModel.clearOffset()
    }


    when (uiState) {
        is UiState.Loading -> Loading()

        is UiState.Success -> {
            val response = (uiState as UiState.Success<List<Challenger.Card>>).response
            ChallengerList(challengerList = response, onClickItemListener = {navController.navigate(Routes.Details.createRoute("256386480080011473"))})
        }

        is UiState.Error -> {
            Error(
                title = "Ops, ocorreu um problema ao carregar os desafios.",
                onClickRetryListener ={}
            )
        }

        else -> {}
    }
}