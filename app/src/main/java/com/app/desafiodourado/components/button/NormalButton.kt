package com.app.desafiodourado.components.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.app.desafiodourado.theme.BackgroundTransparent
import com.app.desafiodourado.theme.BrowLight
import com.app.desafiodourado.theme.CustomDimensions

@Composable
fun NormalButton(
    modifier: Modifier = Modifier,
    title: String,
    titleColor: Color? = null,
    containerColor: Color? = null,
    loading: Boolean = false,
    onButtonListener: () -> Unit,
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(CustomDimensions.padding50),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor ?: BrowLight,
            disabledContainerColor = BackgroundTransparent
        ),
        enabled = !loading,
        onClick = onButtonListener,
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = Color.White, modifier = Modifier.size(
                    width = CustomDimensions.padding20,
                    height = CustomDimensions.padding20
                )
            )
        } else {
            Text(
                text = title,
                color = titleColor ?: Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}