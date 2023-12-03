package com.app.desafiodourado.feature.details.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.app.desafiodourado.R
import com.app.desafiodourado.components.background.Background
import com.app.desafiodourado.components.button.CustomButton
import com.app.desafiodourado.components.image.ImageComponent
import com.app.desafiodourado.components.states.Error
import com.app.desafiodourado.components.states.Loading
import com.app.desafiodourado.components.toolbar.ToolbarCustom
import com.app.desafiodourado.core.utils.UiState
import com.app.desafiodourado.feature.home.ui.model.Challenger.Card
import com.app.desafiodourado.ui.theme.CustomDimensions
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(
    navController: NavController,
    challengerId: String?,
    viewModel: DetailsViewModel = koinViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(key1 = challengerId) {
        challengerId?.let { viewModel.getChallengers(it) }
    }

    val uiState by viewModel.challengerState.observeAsState()

    when (uiState) {
        is UiState.Loading -> Loading()

        is UiState.Success -> {
            val response = (uiState as UiState.Success<Card>).response
            DetailsComponent(
                challenger = response,
                onClickSubmitListener = {}
            )
        }

        else -> {
            Error(
                title = "Ops, ocorreu um problema ao carregar os desafios.",
                onClickRetryListener = {}
            )
        }
    }
}

@Composable
fun DetailsComponent(
    challenger: Card,
    onClickSubmitListener: () -> Unit
) {
    val image = painterResource(id = R.drawable.default_redux)
    val imageGold = painterResource(id = R.drawable.golld_challenger_redux)

    Background {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding()
        ) {
            val (
                toolbar,
                imgCard,
                txtDetails,
                imgAward,
                txtAward,
                btOpenAward
            ) = createRefs()

            ToolbarCustom(
                modifier = Modifier.constrainAs(toolbar) {
                    top.linkTo(parent.top)
                },
                title = "Detalhes do desafio",
                onNavigationListener = {},
                onChallengerListener = {}
            )

            Image(
                painter = if(challenger.type == "NORMAL") image else imageGold,
                contentDescription = "Background Image",
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
                text = challenger.details,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )

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
                url = challenger.awardImage,
                onCLickImageListener = {}
            )

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
                coin = challenger.value,
                onClickListener = {
                    onClickSubmitListener()
                }
            )
        }
    }
}

@Preview
@Composable
fun DetailsScreenPreview() {
    val model = Card(
        image = "https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf",
        completeImage = "https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9",
        type = "NORMAL",
        details = "Essa carta é considerada comum, nela tem chance de vir algum presente ou item para sua coleção, contudo tem chances de não vir nada",
        complete = false,
        value = 200,
        award = "+ 1 Foto de recordação",
        awardImage = "https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e"
    )
    DetailsComponent(
        challenger = model,
        onClickSubmitListener = {}
    )
}