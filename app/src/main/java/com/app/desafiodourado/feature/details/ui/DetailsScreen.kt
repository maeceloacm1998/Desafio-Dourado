package com.app.desafiodourado.feature.details.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.desafiodourado.R
import com.app.desafiodourado.components.background.Background
import com.app.desafiodourado.components.button.CustomButton
import com.app.desafiodourado.components.image.ImageComponent
import com.app.desafiodourado.components.snackbar.CustomSnackbar
import com.app.desafiodourado.components.snackbar.SnackbarCustomType.ERROR
import com.app.desafiodourado.components.toolbar.ToolbarCustom
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.theme.CustomDimensions
import com.app.desafiodourado.theme.Success

@Composable
fun DetailsScreen(
    uiState: DetailsUiState.HasChallengersDetails,
    snackbarHostState: SnackbarHostState,
    onClickSubmitListener: (challengerSelected: Challenger.Card) -> Unit,
    onBack: () -> Unit
) {
    DetailsComponent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onClickSubmitListener = onClickSubmitListener,
        onBack = onBack
    )
}

@Composable
fun DetailsComponent(
    uiState: DetailsUiState.HasChallengersDetails,
    snackbarHostState: SnackbarHostState,
    onClickSubmitListener: (challengerSelected: Challenger.Card) -> Unit,
    onBack: () -> Unit,
) {
    val selectedChallenger: Challenger.Card = uiState.selectedChallenger

    Scaffold(
        snackbarHost = {
            CustomSnackbar(
                snackbarHostState = snackbarHostState,
                snackbarType = ERROR
            )
        }
    ) { contentPadding ->
        Background {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                val (
                    toolbar,
                    imgCard,
                    txtDetails,
                    imgAward,
                    txtShowAward,
                    txtAward,
                    btOpenAward
                ) = createRefs()

                ToolbarCustom(
                    modifier = Modifier.constrainAs(toolbar) {
                        top.linkTo(parent.top)
                    },
                    title = "Detalhes do desafio",
                    onNavigationListener = {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        onBack()
                    },
                    onMissionsListener = {},
                )

                ImageComponent(
                    url = if (selectedChallenger.complete) selectedChallenger.completeImage else selectedChallenger.image,
                    modifier = Modifier
                        .size(
                            width = CustomDimensions.padding150,
                            height = CustomDimensions.padding250
                        )
                        .padding(vertical = CustomDimensions.padding10)
                        .constrainAs(imgCard) {
                            top.linkTo(toolbar.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    onCLickImageListener = {},
                    contentScale = ContentScale.Inside
                )

                Text(
                    modifier = Modifier
                        .padding(
                            vertical = CustomDimensions.padding30,
                            horizontal = CustomDimensions.padding20
                        )
                        .constrainAs(txtDetails) {
                            top.linkTo(imgCard.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    text = selectedChallenger.details,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )

                if (selectedChallenger.complete) {
                    Text(
                        modifier = Modifier
                            .constrainAs(txtShowAward) {
                                top.linkTo(txtDetails.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(btOpenAward.top)
                            }
                            .padding(
                                start = CustomDimensions.padding16,
                                end = CustomDimensions.padding16,
                            ),
                        text = selectedChallenger.award,
                        textAlign = TextAlign.Center,
                        color = Success,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        modifier = Modifier
                            .constrainAs(txtAward) {
                                top.linkTo(txtShowAward.bottom)
                                start.linkTo(txtShowAward.start)
                                end.linkTo(txtShowAward.end)
                            }
                            .padding(
                                start = CustomDimensions.padding16,
                                end = CustomDimensions.padding16,
                            ),
                        text = stringResource(id = R.string.dialog_award_title),
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                } else {
                    ImageComponent(
                        modifier = Modifier
                            .size(
                                width = CustomDimensions.padding80,
                                height = CustomDimensions.padding80
                            )
                            .padding(top = CustomDimensions.padding16)
                            .constrainAs(imgAward) {
                                top.linkTo(txtDetails.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        url = selectedChallenger.awardImage
                    ) {}

                    Text(
                        modifier = Modifier
                            .constrainAs(txtAward) {
                                top.linkTo(imgAward.bottom)
                                start.linkTo(imgAward.start)
                                end.linkTo(imgAward.end)
                            }
                            .padding(
                                start = CustomDimensions.padding16,
                                end = CustomDimensions.padding16,
                            ),
                        text = stringResource(id = R.string.dialog_award_title),
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                CustomButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = CustomDimensions.padding30,
                            horizontal = CustomDimensions.padding20
                        )
                        .height(CustomDimensions.padding50)
                        .constrainAs(btOpenAward) {
                            bottom.linkTo(parent.bottom)
                        },
                    coin = selectedChallenger.value,
                    isSuccess = selectedChallenger.complete,
                    onClickListener = { onClickSubmitListener(selectedChallenger) }
                )
            }
        }
    }
}