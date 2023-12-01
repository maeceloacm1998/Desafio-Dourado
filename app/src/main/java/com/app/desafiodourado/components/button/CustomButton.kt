package com.app.desafiodourado.components.button

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.desafiodourado.R
import com.app.desafiodourado.ui.theme.BrowLight
import com.app.desafiodourado.ui.theme.CustomDimensions
import com.app.desafiodourado.ui.theme.Success

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    isSuccess: Boolean = false,
    coin: Int,
    onClickListener: () -> Unit,
) {
    var state by rememberSaveable { mutableStateOf(isSuccess) }
    val transition = updateTransition(state, label = "ButtonTransition")
    val backgroundColor by transition.animateColor(label = "BackgroundColorTransition") { state ->
        if (state) Success else Color.Transparent
    }
    val color by transition.animateColor(label = "ColorTransition") { state ->
        if (state) Success else BrowLight
    }
    val contentAlpha by transition.animateFloat(label = "ContentAlphaTransition") { state ->
        if (state) 0f else 1f
    }
    val iconAlpha by transition.animateFloat(label = "IconAlphaTransition") { state ->
        if (state) 1f else 0f
    }

    OutlinedButton(
        modifier = modifier,
        onClick = { onClickListener() },
        border = BorderStroke(
            color = color,
            width = CustomDimensions.padding1
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor
        )
    ) {
        ConstraintLayout {
            val (icon, image, text) = createRefs()

            Icon(
                imageVector = Icons.Filled.CheckCircle,
                tint = Color.White,
                contentDescription = "Localized description",
                modifier = Modifier
                    .alpha(iconAlpha)
                    .constrainAs(icon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Image(
                painter = painterResource(id = R.drawable.coin),
                contentDescription = "coin",
                modifier = Modifier
                    .padding(end = CustomDimensions.padding5)
                    .size(height = CustomDimensions.padding24, width = CustomDimensions.padding24)
                    .alpha(contentAlpha)
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
                    .alpha(contentAlpha)
                    .constrainAs(text) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(image.end)
                    },
            )
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