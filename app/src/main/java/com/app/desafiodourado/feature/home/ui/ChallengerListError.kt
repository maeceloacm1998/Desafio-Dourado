package com.app.desafiodourado.feature.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.app.desafiodourado.R

@Composable
fun ChallengerListError() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.challenger_list_error_title),
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview
@Composable
fun ChallengerListErrorPreview() {
    ChallengerListError()
}