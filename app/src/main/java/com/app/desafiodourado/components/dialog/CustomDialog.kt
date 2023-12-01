package com.app.desafiodourado.components.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import com.app.desafiodourado.ui.theme.Background
import com.app.desafiodourado.ui.theme.CustomDimensions

@Composable
fun CustomDialog(
    showDialog: Boolean,
    containerColor: Color = Background,
    onDismissDialog: () -> Unit,
    foreground: @Composable () -> Unit
) {
    if(showDialog) {
        Dialog(onDismissRequest = { onDismissDialog() }) {
            Surface(
                shape = RoundedCornerShape(CustomDimensions.padding16),
                color = containerColor
            ) {
                foreground()
            }
        }
    }
}