package com.app.desafiodourado.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.desafiodourado.R
import com.app.desafiodourado.theme.BrowLight
import com.app.desafiodourado.theme.CustomDimensions
import com.app.desafiodourado.theme.Success

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    isSuccess: Boolean = false,
    coin: Int,
    onClickListener: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier,
        onClick = { onClickListener() },
        enabled = !isSuccess,
        border = BorderStroke(
            color = if (isSuccess) Success else BrowLight,
            width = CustomDimensions.padding1
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSuccess) Success else Color.Transparent,
            disabledContainerColor = if (isSuccess) Success else Color.Transparent
        )
    ) {
        ConstraintLayout {
            val (icon, image, text) = createRefs()

            if (isSuccess) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    tint = Color.White,
                    contentDescription = "Localized description",
                    modifier = Modifier
                        .constrainAs(icon) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.coin),
                    contentDescription = "coin",
                    modifier = Modifier
                        .padding(end = CustomDimensions.padding5)
                        .size(
                            height = CustomDimensions.padding24,
                            width = CustomDimensions.padding24
                        )
                        .constrainAs(image) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                        },
                )

                Text(
                    text = coin.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .constrainAs(text) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(image.end)
                        },
                )
            }
        }
    }
}


@Preview
@Composable
fun CustomButtonPreview() {
    CustomButton(
        isSuccess = false,
        coin = 13000,
        onClickListener = {}
    )
}

@Preview
@Composable
fun CustomButtonSuccessPreview() {
    CustomButton(
        isSuccess = true,
        coin = 13000,
        onClickListener = {}
    )
}