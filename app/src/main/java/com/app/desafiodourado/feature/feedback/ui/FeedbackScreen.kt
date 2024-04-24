package com.app.desafiodourado.feature.feedback.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.desafiodourado.R
import com.app.desafiodourado.components.background.Background
import com.app.desafiodourado.components.button.NormalButton
import com.app.desafiodourado.components.chips.ChipsCustom
import com.app.desafiodourado.components.snackbar.SnackbarCustomType.ERROR
import com.app.desafiodourado.components.states.Error
import com.app.desafiodourado.components.states.Loading
import com.app.desafiodourado.components.textfield.TextFieldCustom
import com.app.desafiodourado.components.toolbar.ToolbarCustom
import com.app.desafiodourado.feature.feedback.ui.FeedbackViewModelUiState.HasFeedbackTypes
import com.app.desafiodourado.feature.feedback.ui.FeedbackViewModelUiState.NoHasFeedbackTypes
import com.app.desafiodourado.feature.feedback.ui.models.FeedbackTypes
import com.app.desafiodourado.theme.Error
import com.app.desafiodourado.theme.CustomDimensions

@Composable
fun FeedbackScreen(
    uiState: FeedbackViewModelUiState,
    snackbarHostState: SnackbarHostState,
    onChangeFeedbackListener: (feedbackText: String) -> Unit,
    onClickFeedbackTypeListener: (FeedbackTypes) -> Unit,
    onClickFinishFeedback: () -> Unit,
    onBack: () -> Unit
) {
    LoadingContent(
        uiState = uiState,
        isEmpty = uiState is NoHasFeedbackTypes && !uiState.isLoading,
        isLoading = uiState.isLoading,
        emptyContent = {
            Error(
                title = stringResource(id = R.string.load_error_feedback),
                onClickRetryListener = {}
            )
        },
        content = { state ->
            FeedbackContainer(
                uiState = state,
                snackBarHostState = snackbarHostState,
                onChangeFeedbackListener = onChangeFeedbackListener,
                onClickFeedbackTypeListener = onClickFeedbackTypeListener,
                onClickFinishFeedback = onClickFinishFeedback,
                onBack = onBack
            )
        }
    )
}

@Composable
private fun LoadingContent(
    uiState: FeedbackViewModelUiState,
    isEmpty: Boolean,
    isLoading: Boolean,
    emptyContent: @Composable () -> Unit,
    content: @Composable (state: HasFeedbackTypes) -> Unit
) {
    when {
        isLoading -> Loading()
        isEmpty -> emptyContent()
        else -> {
            check(uiState is HasFeedbackTypes)
            content(uiState)
        }
    }
}

@Composable
fun FeedbackContainer(
    uiState: HasFeedbackTypes,
    snackBarHostState: SnackbarHostState,
    onChangeFeedbackListener: (feedbackText: String) -> Unit,
    onClickFeedbackTypeListener: (FeedbackTypes) -> Unit,
    onClickFinishFeedback: () -> Unit,
    onBack: () -> Unit,
) {
    Background(
        snackbarHostState = snackBarHostState,
        snackbarType = uiState.snackBarType
    ){
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val (tCFeedback, txtFeedback, tfFeedback, ftlFeedback, btnFeedback) = createRefs()

            ToolbarCustom(
                modifier = Modifier.constrainAs(tCFeedback) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                title = stringResource(id = R.string.feedback_screen_toolbar_title),
                onNavigationListener = onBack,
                onMissionsListener = {},
            )

            Text(
                modifier = Modifier
                    .constrainAs(txtFeedback) {
                        top.linkTo(tCFeedback.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(horizontal = CustomDimensions.padding24),
                text = stringResource(id = R.string.feedback_screen_description),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier
                    .padding(top = CustomDimensions.padding20)
            )

            TextFieldCustom(
                modifier = Modifier
                    .constrainAs(tfFeedback) {
                        top.linkTo(txtFeedback.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .padding(
                        horizontal = CustomDimensions.padding24,
                        vertical = CustomDimensions.padding16
                    ),
                label = stringResource(id = R.string.feedback_screen_label),
                error = uiState.isErrorFeedbackTextIsEmpty,
                errorText = stringResource(id = R.string.feedback_screen_error_text),
                isSingleLine = false,
                maxLines = 12,
                maxLength = 200,
                onChangeListener = { text ->
                    onChangeFeedbackListener(text)
                }
            )

            FeedbackTypesList(
                modifier = Modifier.constrainAs(ftlFeedback) {
                    top.linkTo(tfFeedback.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                feedbackTypesList = uiState.feedbackTypesList,
                isError = uiState.isErrorFeedbackTypesNotSelected,
                onClickFeedbackTypeListener = onClickFeedbackTypeListener
            )

            NormalButton(
                modifier = Modifier
                    .constrainAs(btnFeedback) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(CustomDimensions.padding24),
                title = stringResource(id = R.string.feedback_screen_button),
                loading = uiState.isLoadingFinishFeedback,
                onButtonListener = onClickFinishFeedback
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FeedbackTypesList(
    modifier: Modifier = Modifier,
    feedbackTypesList: List<FeedbackTypes>,
    isError: Boolean = false,
    onClickFeedbackTypeListener: (FeedbackTypes) -> Unit
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = CustomDimensions.padding24),
    ) {
        feedbackTypesList.forEach {
            ChipsCustom(
                modifier = Modifier.padding(end = CustomDimensions.padding10),
                isSelected = it.isSelected,
                title = it.feedbackType,
                onClickChipListener = { onClickFeedbackTypeListener(it) }
            )
        }
        AnimatedVisibility(visible = isError) {
            Text(
                text = stringResource(
                    id = R.string.feedback_screen_error_types
                ),
                color = Error,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun FeedbackScreenPreview() {
    FeedbackScreen(
        uiState = HasFeedbackTypes(
            isLoading = false,
            isErrorFeedbackTypesNotSelected = false,
            isErrorFeedbackTextIsEmpty = false,
            isLoadingFinishFeedback = false,
            snackBarType = ERROR,
            feedbackText = "",
            feedbackTypesList = mutableListOf()
        ),
        onChangeFeedbackListener = {},
        onClickFeedbackTypeListener = {},
        onClickFinishFeedback = {},
        snackbarHostState = SnackbarHostState(),
        onBack = {}
    )
}