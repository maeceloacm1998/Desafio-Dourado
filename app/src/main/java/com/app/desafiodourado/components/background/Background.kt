package com.app.desafiodourado.components.background

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.desafiodourado.R
import com.app.desafiodourado.components.snackbar.CustomSnackbar
import com.app.desafiodourado.components.snackbar.SnackbarCustomType

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Background(
    snackbarHostState: SnackbarHostState? = null,
    snackbarType: SnackbarCustomType = SnackbarCustomType.SUCCESS,
    foreground: @Composable () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (background, foreground) = createRefs()
        val image = painterResource(id = R.drawable.background)

        Scaffold(
            snackbarHost = {
                snackbarHostState?.let {
                    CustomSnackbar(snackbarHostState = it, snackbarType)
                }
            }
        ) {
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
}

@Preview
@Composable
fun BackgroundPreview() {
    Background(snackbarHostState = null, snackbarType = SnackbarCustomType.SUCCESS) {
        Box(
            Modifier
                .size(height = 100.dp, width = 100.dp)
                .background(Color.Red)
        ) {}
    }
}