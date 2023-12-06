package com.app.desafiodourado.components.checkbox

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.app.desafiodourado.R
import com.app.desafiodourado.feature.home.ui.model.Missions
import com.app.desafiodourado.feature.initial.data.missions
import com.app.desafiodourado.theme.CustomDimensions
import com.app.desafiodourado.theme.PurpleLight
import com.app.desafiodourado.theme.Success

@Composable
fun CustomCheckbox(
    mission: Missions.MissionsModel,
    onChangeCheckbox: (checked: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val (checkedState, onStateChange) = rememberSaveable { mutableStateOf(mission.isChecked) }
    Row(
        modifier
            .fillMaxWidth()
            .toggleable(
                value = checkedState,
                onValueChange = {
                    if(!checkedState) {
                        onChangeCheckbox(true)
                        onStateChange(true)
                    }
                },
                role = Role.Checkbox
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            modifier = Modifier.weight(5f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Checkbox(
                modifier = Modifier.weight(0.5f),
                checked = checkedState,
                colors = CheckboxDefaults.colors(
                    checkedColor = Success,
                    uncheckedColor = PurpleLight
                ),
                onCheckedChange = null
            )
            Column(modifier = Modifier.weight(5f)) {
                Text(
                    text = mission.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    textDecoration = if (checkedState) TextDecoration.LineThrough else TextDecoration.None,
                    modifier = Modifier
                        .padding(start = CustomDimensions.padding8)
                        .fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .weight(5f)
                            .padding(start = CustomDimensions.padding8),
                        progress = if (checkedState) 1f else 0.1f,
                        color = Success,
                        trackColor = Color.DarkGray,
                    )

                    Text(
                        text = if (checkedState) "100 %" else "0 %",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        modifier = Modifier
                            .weight(2f)
                            .padding(
                                start = CustomDimensions.padding16
                            )
                    )
                }
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End,
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
                text = mission.coinValue.toString(),
                textDecoration = if (checkedState) TextDecoration.LineThrough else TextDecoration.None,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Preview
@Composable
fun CustomCheckboxPreview() {
    CustomCheckbox(
        mission = missions[25],
        onChangeCheckbox = {}
    )
}