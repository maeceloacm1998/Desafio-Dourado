package com.app.desafiodourado.components.toolbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.app.desafiodourado.R
import com.app.desafiodourado.theme.Background
import com.app.desafiodourado.theme.BrowLight
import com.app.desafiodourado.theme.CustomDimensions
import com.app.desafiodourado.theme.PurpleLight
import com.app.desafiodourado.theme.RedDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolbarCustom(
    modifier: Modifier = Modifier,
    title: String,
    badgeCount: Int = 0,
    showNavigationIcon: Boolean = true,
    showBadgeCount: Boolean = false,
    onChallengerListener: () -> Unit,
    onNavigationListener: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = title,
                color = Color.White
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Background,
            navigationIconContentColor = Background
        ),
        navigationIcon = {
            if (showNavigationIcon) {
                IconButton(onClick = { onNavigationListener() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "voltar",
                        tint = BrowLight
                    )
                }
            } else {
                val image = painterResource(id = R.drawable.challenger)

                Image(
                    painter = image,
                    contentDescription = "Background Image",
                    modifier = Modifier
                        .padding(
                            end = CustomDimensions.padding10,
                            start = CustomDimensions.padding20
                        )
                        .size(
                            height = CustomDimensions.padding50,
                            width = CustomDimensions.padding50
                        ),
                    contentScale = ContentScale.Crop,
                )
            }
        },
        actions = {
            if (showBadgeCount) {
                Box(
                    modifier = Modifier.padding(end = CustomDimensions.padding20)
                ) {
                    IconButton(
                        onClick = { onChallengerListener() },
                        modifier = Modifier
                            .size(CustomDimensions.padding50)
                            .clip(CircleShape)
                            .background(color = PurpleLight)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.EmojiEvents,
                            tint = BrowLight,
                            contentDescription = "Localized description"
                        )
                    }
                    if (badgeCount > 0) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = CustomDimensions.padding10)
                                .size(CustomDimensions.padding24)
                                .background(RedDark, CircleShape)
                        ) {
                            Text(
                                text = badgeCount.toString(),
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        },
    )
}

@Preview
@Composable
fun ToolbarPreview() {
    ToolbarCustom(
        title = "Premios Misteriosos",
        badgeCount = 2,
        showNavigationIcon = false,
        showBadgeCount = true,
        onChallengerListener = {},
        onNavigationListener = {}
    )
}

@Preview
@Composable
fun ToolbarNavigationPreview() {
    ToolbarCustom(
        title = "Premios Misteriosos",
        onChallengerListener = {},
        onNavigationListener = {}
    )
}