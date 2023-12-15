package com.app.desafiodourado.components.image

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

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
            url, "image",
            transition = CrossFade
        )
    }
}