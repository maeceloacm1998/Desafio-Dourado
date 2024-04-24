package com.app.desafiodourado.feature.feedback.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

    FeedbackRoute(
        uiState = uiState,
        onChangeFeedbackListener = { feedbackText ->
            viewModel.onChangeFeedbackTextUpdate(feedbackText)
        },
        onClickFeedbackTypeListener = { feedbackType -> },
        onClickFinishFeedback = { viewModel.onFinishFeedback() },
        onBack = { navController.popBackStack() }
    )
}

@Composable
fun FeedbackRoute(
    uiState: FeedbackViewModelUiState,
    onChangeFeedbackListener: (feedbackText: String) -> Unit,
    onClickFeedbackTypeListener: (FeedbackTypes) -> Unit,
    onClickFinishFeedback: () -> Unit,
    onBack: () -> Unit
) {
    FeedbackScreen(
        uiState = uiState,
        onChangeFeedbackListener = onChangeFeedbackListener,
        onClickFeedbackTypeListener = onClickFeedbackTypeListener,
        onClickFinishFeedback = onClickFinishFeedback,
        onBack = onBack,
    )
}