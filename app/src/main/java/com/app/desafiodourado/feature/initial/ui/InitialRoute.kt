package com.app.desafiodourado.feature.initial.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.app.desafiodourado.core.routes.Routes
import org.koin.androidx.compose.koinViewModel

@Composable
fun InitialRoute(
    navController: NavController,
    viewModel: InitialViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    InitialRoute(
        uiState = uiState,
        onSubmitButton = { userName ->
//            viewModel.createUser(
//                userName,
//                navController
//            )
            navController.navigate(Routes.Feedback.route)
        })
}

@Composable
fun InitialRoute(
    uiState: InitialViewModelUiState,
    onSubmitButton: (userName: String) -> Unit
) {
    check(uiState is InitialViewModelUiState.Data)
    InitialScreen(
        uiState = uiState,
        onSubmitButton = onSubmitButton
    )
}