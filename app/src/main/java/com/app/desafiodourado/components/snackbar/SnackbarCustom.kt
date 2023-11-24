package com.app.desafiodourado.components.snackbar

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults.contentColor
import androidx.compose.material3.SnackbarDefaults.shape
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomSnackbar(
    snackbarHostState: SnackbarHostState,
    snackbarType: SnackbarCustomType = SnackbarCustomType.SUCCESS,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier) {

        SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { data ->
                Snackbar(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    containerColor = snackbarType.background,
                    contentColor = contentColor,
                    shape = shape,
                    actionOnNewLine = false,
                    snackbarData = data
                )
            }
        )
    }
}