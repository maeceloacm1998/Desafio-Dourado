package com.app.desafiodourado.components.states

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.desafiodourado.ui.theme.BrowLight
import com.app.desafiodourado.ui.theme.CustomDimensions

@Composable
fun Error(title: String, onClickRetryListener: () -> Unit) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (txtTitle, btRetry) = createRefs()

        Text(
            modifier = Modifier
                .padding(
                    horizontal = CustomDimensions.padding20, vertical = CustomDimensions.padding14
                )
                .constrainAs(txtTitle) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                },
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        TextButton(modifier = Modifier.constrainAs(btRetry) {
            top.linkTo(txtTitle.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        }, onClick = { onClickRetryListener() }) {
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