package com.app.desafiodourado.feature.feedback.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.app.desafiodourado.components.background.Background
import com.app.desafiodourado.components.button.NormalButton
import com.app.desafiodourado.components.chips.ChipsCustom
import com.app.desafiodourado.components.textfield.TextFieldCustom
import com.app.desafiodourado.components.toolbar.ToolbarCustom
import com.app.desafiodourado.theme.CustomDimensions

@Composable
fun FeedbackScreen(
    uiState: FeedbackViewModelUiState.HasFeedbackTypes,
    onChangeFeedbackListener: (feedbackText: String) -> Unit,
    onClickFinishFeedback: () -> Unit,
    onBack: () -> Unit
) {
    Background {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f)
            ) {
                ToolbarCustom(
                    title = "Feedback do Usuário",
                    onNavigationListener = onBack,
                    onMissionsListener = {},
                )

                Text(
                    modifier = Modifier.padding(horizontal = CustomDimensions.padding24),
                    text = "Sua opinião é muito importante para nós. Por favor, compartilhe suas ideias ou reporte qualquer problema que você tenha encontrado.",
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
                        .weight(2f)
                        .fillMaxWidth()
                        .padding(horizontal = CustomDimensions.padding24),
                    label = "Digite sua sugestão aqui…",
                    onChangeListener = { text ->
                        onChangeFeedbackListener(text)
                    }
                )

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.8f)
                        .padding(horizontal = CustomDimensions.padding24),
                ) {
                    uiState.feedbackTypesList.forEach {
                        ChipsCustom(
                            isSelected = true,
                            onClickChipListener = { /*TODO*/ }
                        )
                    }

                }
            }

            NormalButton(
                modifier = Modifier.padding(CustomDimensions.padding24),
                title = "Enviar Feedback",
                loading = uiState.isLoading,
                onButtonListener = onClickFinishFeedback
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
        onClickFinishFeedback = {},
        onBack = {}
    )
}