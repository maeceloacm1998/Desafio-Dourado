package com.app.desafiodourado.feature.home.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.app.desafiodourado.components.background.Background
import com.app.desafiodourado.components.coinview.CoinView
import com.app.desafiodourado.ui.theme.CustomDimensions

@Composable
fun HomeScreen(navController: NavController) {
    HomeComponents()
}

@Composable
fun HomeComponents() {
    Background {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (txtVersion, coinView) = createRefs()
            createHorizontalChain(txtVersion, coinView, chainStyle = ChainStyle.SpreadInside)
            Text(
                modifier = Modifier
                    .padding(
                        horizontal = CustomDimensions.padding20,
                        vertical = CustomDimensions.padding14
                    )
                    .constrainAs(txtVersion) {
                        end.linkTo(coinView.start)
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                text = "v1.0.0",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            CoinView(
                modifier = Modifier
                    .padding(
                        horizontal = CustomDimensions.padding20,
                        vertical = CustomDimensions.padding14
                    )
                    .constrainAs(coinView) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        start.linkTo(txtVersion.end)
                    },
                coin = 1205
            )
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    HomeComponents()
}