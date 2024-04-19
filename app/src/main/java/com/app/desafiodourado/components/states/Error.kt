package com.app.desafiodourado.components.states

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.app.desafiodourado.theme.BrowLight
import com.app.desafiodourado.theme.CustomDimensions

@Composable
fun Error(title: String, onClickRetryListener: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(vertical = CustomDimensions.padding14),
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        TextButton(
            onClick = onClickRetryListener
        ) {
            Text(
                modifier = Modifier.padding(end = CustomDimensions.padding10),
                text = "Tentar novamente",
                style = MaterialTheme.typography.titleSmall,
                color = BrowLight,
                textAlign = TextAlign.Center
            )
            Icon(
                imageVector = Icons.Filled.Update,
                tint = BrowLight,
                contentDescription = "Localized description"
            )
        }
    }
}

@Preview
@Composable
fun ErrorPreview() {
    Error(
        title = "Erro ao carregar os desafios",
        onClickRetryListener = {}
    )
}