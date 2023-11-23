package com.app.desafiodourado.components.background

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.desafiodourado.R

@Composable
fun Background(foreground: @Composable () -> Unit) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (background, foreground) = createRefs()
        val image = painterResource(id = R.drawable.background)

        Image(
            painter = image,
            contentDescription = "Background Image",
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(background) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
            contentScale = ContentScale.Crop,
        )

        Box(modifier = Modifier
            .fillMaxSize()
            .constrainAs(foreground) {
                top.linkTo(background.top)
                end.linkTo(background.end)
                bottom.linkTo(background.bottom)
                start.linkTo(background.start)
            }) {
            foreground()
        }
    }
}

@Preview
@Composable
fun BackgroundPreview() {
    Background {
        Box(
            Modifier
                .size(height = 100.dp, width = 100.dp)
                .background(Color.Red)
        ) {}
    }
}