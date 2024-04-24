package com.app.desafiodourado.feature.feedback.ui

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import com.app.desafiodourado.components.states.Error
import com.app.desafiodourado.components.states.Loading
import com.app.desafiodourado.components.textfield.TextFieldCustom
import com.app.desafiodourado.components.toolbar.ToolbarCustom
import com.app.desafiodourado.feature.feedback.ui.models.FeedbackTypes
import com.app.desafiodourado.theme.CustomDimensions

@Composable
fun FeedbackScreen(
    uiState: FeedbackViewModelUiState,
    onChangeFeedbackListener: (feedbackText: String) -> Unit,
    onClickFeedbackTypeListener: (FeedbackTypes) -> Unit,
    onClickFinishFeedback: () -> Unit,
    onBack: () -> Unit
) {
    LoadingContent(
        isEmpty = uiState is FeedbackViewModelUiState.NoHasFeedbackTypes,
        isLoading = uiState is FeedbackViewModelUiState.HasFeedbackTypes && uiState.isLoading,
        emptyContent = {
            Error(
                title = stringResource(id = R.string.load_error_feedback),
                onClickRetryListener = {}
            )
        },
        content = {
            check(uiState is FeedbackViewModelUiState.HasFeedbackTypes)
            FeedbackContainer(
                uiState = uiState,
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
    isEmpty: Boolean,
    isLoading: Boolean,
    emptyContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    when {
        isLoading -> Loading()
        isEmpty -> emptyContent()
        else -> content()
    }
}

@Composable
fun FeedbackContainer(
    uiState: FeedbackViewModelUiState.HasFeedbackTypes,
    onChangeFeedbackListener: (feedbackText: String) -> Unit,
    onClickFeedbackTypeListener: (FeedbackTypes) -> Unit,
    onClickFinishFeedback: () -> Unit,
    onBack: () -> Unit,
) {
    Background {
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
                    .padding(horizontal = CustomDimensions.padding24, vertical = CustomDimensions.padding16),
                label = stringResource(id = R.string.feedback_screen_label),
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
    }
}

@Preview
@Composable
fun FeedbackScreenPreview() {
    FeedbackScreen(
        uiState = FeedbackViewModelUiState.HasFeedbackTypes(
            isLoading = false,
            isErrorFetchData = false,
            isLoadingFinishFeedback = false,
            feedbackText = "",
            feedbackTypesList = mutableListOf()
        ),
        onChangeFeedbackListener = {},
        onClickFeedbackTypeListener = {},
        onClickFinishFeedback = {},
        onBack = {}
    )
}