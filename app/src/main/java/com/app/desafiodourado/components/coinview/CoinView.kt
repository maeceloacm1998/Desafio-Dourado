package com.app.desafiodourado.components.coinview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.desafiodourado.R
import com.app.desafiodourado.theme.BlueDark
import com.app.desafiodourado.theme.BrowLight
import com.app.desafiodourado.theme.CustomDimensions

@Composable
fun CoinView(modifier: Modifier = Modifier, coin: Int) {

    ConstraintLayout(
        modifier = modifier
            .background(color = BlueDark)
            .border(
                width = CustomDimensions.padding2,
                color = BrowLight,
                shape = RoundedCornerShape(CustomDimensions.padding10)
            )
            .clip(RoundedCornerShape(CustomDimensions.padding10))
            .size(
                height = CustomDimensions.padding30,
                width = CustomDimensions.padding80
            )
            .padding(horizontal = CustomDimensions.padding10),
    ) {
        val (img, text) = createRefs()
        createHorizontalChain(img, text, chainStyle = ChainStyle.SpreadInside)
        Image(
            painter = painterResource(id = R.drawable.coin),
            contentDescription = "Background Image",
            modifier = Modifier
                .constrainAs(img) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(text.start)
                }
                .size(height = CustomDimensions.padding24, width = CustomDimensions.padding24),
        )
        Text(
            modifier = Modifier
                .constrainAs(text) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(img.end)
                    end.linkTo(parent.end)
                },
            text = coin.toString(),
            color = Color.White,
            style = MaterialTheme.typography.titleSmall,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CoinViewPreview() {
    CoinView(modifier = Modifier, coin = 10)
}