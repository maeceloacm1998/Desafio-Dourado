package com.app.desafiodourado.components.snackbar

import androidx.compose.ui.graphics.Color
import com.app.desafiodourado.theme.BrowDark
import com.app.desafiodourado.theme.Error
import com.app.desafiodourado.theme.Success

enum class SnackbarCustomType(val background: Color, val text: Color) {
    SUCCESS(Success, Color.White),
    ERROR(Error, Color.White),
    WARNING(BrowDark, Color.White);
}