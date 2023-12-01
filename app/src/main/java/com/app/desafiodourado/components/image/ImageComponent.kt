package com.app.desafiodourado.components.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.app.desafiodourado.ui.theme.BrowLight
import com.app.desafiodourado.ui.theme.CustomDimensions

@Composable
fun ImageComponent(
    modifier: Modifier = Modifier,
    url: String,
    onCLickImageListener: () -> Unit
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .size(coil.size.Size.ORIGINAL)
            .build()
    )

    Box(
        modifier = modifier.clickable { onCLickImageListener() },
        contentAlignment = Alignment.Center
    ) {
        if (painter.state is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(
                color = BrowLight,
                modifier = Modifier
                    .size(
                        width = CustomDimensions.padding10,
                        height = CustomDimensions.padding10
                    )
            )
        } else {
            Image(
                modifier = modifier,
                painter = painter,
                contentDescription = "photo"
            )
        }
    }
}

@Preview
@Composable
fun ImageComponentPreview() {
    ImageComponent(url = "https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf") {

    }
}