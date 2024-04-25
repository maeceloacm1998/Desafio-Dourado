package com.app.desafiodourado.components.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.app.desafiodourado.theme.CustomDimensions

@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    title: String,
    titleColor: Color = Color.White,
    onClick: () -> Unit,
    icon: ImageVector = Icons.Filled.ArrowBack,
    iconColor: Color = Color.White
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        content = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.padding(end = CustomDimensions.padding10),
                    imageVector = icon,
                    colorFilter = ColorFilter.tint(iconColor),
                    contentDescription = "$title icon",
                )
                Text(
                    text = title,
                    color = titleColor
                )
            }
        }
    )
}

@Preview
@Composable
fun CustomIconButtonPreview() {
    CustomIconButton(title = "Back", onClick = {})
}