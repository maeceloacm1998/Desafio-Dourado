package com.app.desafiodourado.feature.feedback.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.app.desafiodourado.feature.feedback.ui.models.FeedbackTypes
import org.koin.compose.koinInject

@Composable
fun FeedbackRoute(
    navController: NavController,
    viewModel: FeedbackViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    FeedbackRoute(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onChangeFeedbackListener = { feedbackText ->
            viewModel.onChangeFeedbackTextUpdate(feedbackText)
        },
        onClickFeedbackTypeListener = { feedbackType ->
            viewModel.onCheckFeedbackType(feedbackType)
        },
        onClickFinishFeedback = {
            viewModel.onFinishFeedback(
                snackbarHostState = snackbarHostState,
                goBackScreen = { navController.popBackStack() }
            )
        },
        onBack = { navController.popBackStack() }
    )
}

@Composable
fun FeedbackRoute(
    uiState: FeedbackViewModelUiState,
    snackbarHostState: SnackbarHostState,
    onChangeFeedbackListener: (feedbackText: String) -> Unit,
    onClickFeedbackTypeListener: (FeedbackTypes) -> Unit,
    onClickFinishFeedback: () -> Unit,
    onBack: () -> Unit
) {
    FeedbackScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onChangeFeedbackListener = onChangeFeedbackListener,
        onClickFeedbackTypeListener = onClickFeedbackTypeListener,
        onClickFinishFeedback = onClickFinishFeedback,
        onBack = onBack,
    )
}