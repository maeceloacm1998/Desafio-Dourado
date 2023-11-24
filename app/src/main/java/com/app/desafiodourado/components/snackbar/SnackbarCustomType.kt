package com.app.desafiodourado.components.snackbar

import androidx.compose.ui.graphics.Color
import com.app.desafiodourado.ui.theme.BrowDark
import com.app.desafiodourado.ui.theme.Error
import com.app.desafiodourado.ui.theme.Success

enum class SnackbarCustomType(val background: Color, val text: Color) {
    SUCCESS(Success, Color.White),
    ERROR(Error, Color.White),
    WARNING(BrowDark, Color.White);
}