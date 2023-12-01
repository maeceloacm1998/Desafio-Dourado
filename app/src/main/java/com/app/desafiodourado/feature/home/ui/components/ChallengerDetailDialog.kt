package com.app.desafiodourado.feature.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.app.desafiodourado.R
import com.app.desafiodourado.components.button.CustomButton
import com.app.desafiodourado.components.image.ImageComponent
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.ui.theme.Background
import com.app.desafiodourado.ui.theme.CustomDimensions
import com.app.desafiodourado.ui.theme.PurpleLight

@Composable
fun DialogDetails(
    challenger: Challenger.Card,
    onClickCancelListener: () -> Unit,
    onClickSubmitListener: (challenger: Challenger.Card) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(
                horizontal = CustomDimensions.padding16,
                vertical = CustomDimensions.padding16
            )
            .background(Background)
    ) {
        val (
            txtTitle,
            imgCard,
            txtDetails,
            imgAward,
            txtAward,
            btCancel,
            btOpenAward
        ) = createRefs()

        createHorizontalChain(btCancel, btOpenAward, chainStyle = ChainStyle.SpreadInside)

        Text(
            modifier = Modifier
                .constrainAs(txtTitle) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },

            text = stringResource(id = R.string.dialog_title),
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )

        ImageComponent(
            modifier = Modifier
                .size(
                    width = CustomDimensions.padding180,
                    height = CustomDimensions.padding180
                )
                .padding(vertical = CustomDimensions.padding10)
                .constrainAs(imgCard) {
                    top.linkTo(txtTitle.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            url = if (challenger.isComplete) challenger.completeImage else challenger.image,
            onCLickImageListener = {}
        )

        Text(
            modifier = Modifier
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
                    bottom.linkTo(btCancel.top)
                }
                .padding(
                    start = CustomDimensions.padding16,
                    end = CustomDimensions.padding16,
                    bottom = CustomDimensions.padding20
                ),
            text = stringResource(id = R.string.dialog_award_title),
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )

        TextButton(
            modifier = Modifier.constrainAs(btCancel) {
                top.linkTo(txtAward.bottom)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
            },
            onClick = {
                onClickCancelListener()
            }
        ) {
            Text(
                text = stringResource(id = R.string.dialog_cancel_button_title),
                color = PurpleLight,
                style = MaterialTheme.typography.titleMedium
            )
        }

        CustomButton(
            modifier = Modifier.constrainAs(btOpenAward) {
                top.linkTo(txtAward.bottom)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
            },
            coin = challenger.value,
            onClickListener = {
                onClickSubmitListener(challenger)
            }
        )
    }
}

@Preview
@Composable
fun DialogDetailsPreview() {
    DialogDetails(
        challenger = Challenger.Card(),
        onClickCancelListener = {},
        onClickSubmitListener = {})
}