package com.app.desafiodourado.feature.challengerdetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.desafiodourado.R
import com.app.desafiodourado.components.button.CustomButton
import com.app.desafiodourado.components.image.ImageComponent
import com.app.desafiodourado.components.toolbar.ToolbarCustom
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.ui.theme.CustomDimensions

@Composable
fun DetailsScreen(
    challengerItem: Challenger.Card,
    onBack: () -> Unit
) {
    DetailsComponent(
        challenger = challengerItem,
        onClickSubmitListener = {},
        onBack = onBack
    )
}

@Composable
fun DetailsComponent(
    challenger: Challenger.Card,
    onClickSubmitListener: () -> Unit,
    onBack: () -> Unit,
) {
    val image = painterResource(id = R.drawable.default_redux)
    val imageGold = painterResource(id = R.drawable.golld_challenger_redux)

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
            onNavigationListener = onBack,
            onChallengerListener = {}
        )

        Image(
            painter = if (challenger.type == "NORMAL") image else imageGold,
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