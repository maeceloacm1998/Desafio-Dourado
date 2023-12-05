package com.app.desafiodourado.components.checkbox

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.desafiodourado.R
import com.app.desafiodourado.ui.theme.BlueDark
import com.app.desafiodourado.ui.theme.CustomDimensions
import com.app.desafiodourado.ui.theme.PurpleLight
import com.app.desafiodourado.ui.theme.Success

@Preview
@Composable
fun CustomCheckbox() {
    val (checkedState, onStateChange) = rememberSaveable { mutableStateOf(false) }
    Row(
        Modifier
            .fillMaxWidth()
            .toggleable(
                value = checkedState,
                onValueChange = { onStateChange(!checkedState) },
                role = Role.Checkbox
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Checkbox(
                    checked = checkedState,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Success,
                        uncheckedColor = PurpleLight
                    ),
                    onCheckedChange = null
                )
                Column {
                    Text(
                        text = "Option selection",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        textDecoration = if (checkedState) TextDecoration.LineThrough else TextDecoration.None,
                        modifier = Modifier.padding(
                            start = CustomDimensions.padding16
                        )
                    )

                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LinearProgressIndicator(
                            progress = if (checkedState) 1f else 0.1f,
                            color = Success,
                            trackColor = Color.DarkGray,
                            modifier = Modifier.padding(start = CustomDimensions.padding16)
                        )

                        Text(
                            text = if (checkedState) "100 %" else "0 %",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            modifier = Modifier.padding(
                                start = CustomDimensions.padding16
                            )
                        )
                    }
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.coin),
                contentDescription = "Coin Image",
                modifier = Modifier
                    .size(height = CustomDimensions.padding24, width = CustomDimensions.padding24),
            )
            Text(
                modifier = Modifier,
                text = "200",
                textDecoration = if (checkedState) TextDecoration.LineThrough else TextDecoration.None,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}