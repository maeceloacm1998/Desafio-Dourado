package com.app.desafiodourado.components.image

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.app.desafiodourado.theme.BrowLight
import com.app.desafiodourado.theme.CustomDimensions
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageComponent(
    modifier: Modifier = Modifier,
    url: String,
    contentScale: ContentScale = ContentScale.Fit,
    onCLickImageListener: () -> Unit
) {
    Box(
        modifier = modifier.clickable { onCLickImageListener() },
        contentAlignment = Alignment.Center
    ) {
        GlideImage(
            url, "imagem",
            transition = CrossFade,
            loading = placeholder {
                CircularProgressIndicator(
                    color = BrowLight,
                    modifier = Modifier.size(
                        width = CustomDimensions.padding20,
                        height = CustomDimensions.padding20
                    )
                )
            }
        )
    }
}