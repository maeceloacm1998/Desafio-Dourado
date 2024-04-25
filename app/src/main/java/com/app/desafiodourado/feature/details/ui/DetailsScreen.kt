package com.app.desafiodourado.feature.details.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.app.desafiodourado.R
import com.app.desafiodourado.components.background.Background
import com.app.desafiodourado.components.button.CustomButton
import com.app.desafiodourado.components.button.CustomIconButton
import com.app.desafiodourado.components.image.ImageComponent
import com.app.desafiodourado.components.snackbar.CustomSnackbar
import com.app.desafiodourado.components.snackbar.SnackbarCustomType.ERROR
import com.app.desafiodourado.components.toolbar.ToolbarCustom
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.theme.BrowDark
import com.app.desafiodourado.theme.CustomDimensions
import com.app.desafiodourado.theme.Success

@Composable
fun DetailsScreen(
    uiState: DetailsUiState.HasChallengersDetails,
    snackbarHostState: SnackbarHostState,
    onClickSubmitListener: (challengerSelected: Challenger.Card) -> Unit,
    onClickFeedback: () -> Unit,
    onBack: () -> Unit
) {
    DetailsSnackBar(snackbarHostState = snackbarHostState) { contentPadding ->
        DetailsComponent(
            uiState = uiState,
            contentPadding = contentPadding,
            onClickFeedback = onClickFeedback,
            onClickSubmitListener = onClickSubmitListener,
            onBack = onBack
        )
    }
}

@Composable
fun DetailsSnackBar(
    snackbarHostState: SnackbarHostState,
    content: @Composable (paddingValues: PaddingValues) -> Unit,
) {
    Scaffold(
        snackbarHost = {
            CustomSnackbar(
                snackbarHostState = snackbarHostState,
                snackbarType = ERROR
            )
        }
    ) {
        Background {
            content(it)
        }
    }
}

@Composable
fun DetailsComponent(
    uiState: DetailsUiState.HasChallengersDetails,
    contentPadding: PaddingValues,
    onClickFeedback: () -> Unit,
    onClickSubmitListener: (challengerSelected: Challenger.Card) -> Unit,
    onBack: () -> Unit,
) {
    val selectedChallenger: Challenger.Card = uiState.selectedChallenger

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
    ) {
        ToolbarCustom(
            title = "Detalhes do desafio",
            onNavigationListener = onBack,
            onMissionsListener = {},
        )

        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardImageComponent(selectedChallenger = selectedChallenger)

            Text(
                modifier = Modifier
                    .padding(
                        vertical = CustomDimensions.padding30,
                        horizontal = CustomDimensions.padding20
                    ),
                text = selectedChallenger.details,
                textAlign = TextAlign.Center,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
            )

            CardGiftContainer(selectedChallenger = selectedChallenger)
        }

        Column(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            CustomIconButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = CustomDimensions.padding20),
                title = "Tem alguma sugestão?",
                icon = Icons.Outlined.Message,
                iconColor = BrowDark,
                onClick = onClickFeedback
            )

            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = CustomDimensions.padding16,
                        horizontal = CustomDimensions.padding20
                    )
                    .height(CustomDimensions.padding50),
                coin = selectedChallenger.value,
                loading = uiState.isLoading,
                isSuccess = selectedChallenger.complete,
                onClickListener = { onClickSubmitListener(selectedChallenger) }
            )
        }
    }
}

@Composable
fun CardImageComponent(selectedChallenger: Challenger.Card) {
    AnimatedVisibility(visible = selectedChallenger.complete) {
        ImageComponent(
            url = selectedChallenger.completeImage,
            modifier = Modifier
                .size(
                    width = CustomDimensions.padding150,
                    height = CustomDimensions.padding250
                )
                .padding(vertical = CustomDimensions.padding10),
            onCLickImageListener = {},
            contentScale = ContentScale.Inside
        )
    }

    AnimatedVisibility(visible = !selectedChallenger.complete) {
        ImageComponent(
            url = selectedChallenger.image,
            modifier = Modifier
                .size(
                    width = CustomDimensions.padding150,
                    height = CustomDimensions.padding250
                )
                .padding(vertical = CustomDimensions.padding10),
            onCLickImageListener = {},
            contentScale = ContentScale.Inside
        )
    }
}

@Composable
fun CardGiftContainer(selectedChallenger: Challenger.Card) {
    AnimatedVisibility(visible = selectedChallenger.complete) {
        Text(
            modifier = Modifier
                .padding(
                    start = CustomDimensions.padding16,
                    end = CustomDimensions.padding16,
                ),
            text = selectedChallenger.award,
            textAlign = TextAlign.Center,
            color = Success,
            style = MaterialTheme.typography.titleLarge
        )
    }

    AnimatedVisibility(visible = !selectedChallenger.complete) {
        ImageComponent(
            modifier = Modifier
                .size(
                    width = CustomDimensions.padding80,
                    height = CustomDimensions.padding80
                )
                .padding(top = CustomDimensions.padding16),
            url = selectedChallenger.awardImage,
            onCLickImageListener = {}
        )
    }

    Text(
        modifier = Modifier
            .padding(
                start = CustomDimensions.padding16,
                end = CustomDimensions.padding16,
            ),
        text = stringResource(id = R.string.dialog_award_title),
        color = Color.White,
        style = MaterialTheme.typography.titleMedium
    )
}

@Preview
@Composable
fun DetailsScreenPreview() {
    DetailsScreen(
        uiState = DetailsUiState.HasChallengersDetails(
            challengers = listOf(
                Challenger.Card(
                    id = "",
                    image = "https://i.imgur.com/2xZz8Zz.jpg",
                    completeImage = "https://i.imgur.com/2xZz8Zz.jpg",
                    details = "Detalhes do desafio",
                    award = "Recompensa",
                    awardImage = "https://i.imgur.com/2xZz8Zz.jpg",
                    value = 100,
                    complete = false
                )
            ),
            selectedChallenger = Challenger.Card(
                id = "",
                image = "https://i.imgur.com/2xZz8Zz.jpg",
                completeImage = "https://i.imgur.com/2xZz8Zz.jpg",
                details = "Detalhes do desafio",
                award = "Recompensa",
                awardImage = "https://i.imgur.com/2xZz8Zz.jpg",
                value = 100,
                complete = false
            ),
            userCoins = 100,
            isLoading = false,
            errorMessages = null
        ),
        snackbarHostState = SnackbarHostState(),
        onClickFeedback = {},
        onClickSubmitListener = {},
        onBack = {}
    )
}