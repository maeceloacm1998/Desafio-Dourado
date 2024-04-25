package com.app.desafiodourado.feature.missions.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.app.desafiodourado.commons.FakeHomeRepositoryImpl
import com.app.desafiodourado.components.button.CustomIconButton
import com.app.desafiodourado.components.checkbox.CustomCheckbox
import com.app.desafiodourado.core.utils.Result
import com.app.desafiodourado.feature.home.ui.model.Missions
import com.app.desafiodourado.theme.BrowDark
import com.app.desafiodourado.theme.BrowLight
import com.app.desafiodourado.theme.CustomDimensions
import kotlinx.coroutines.runBlocking

@Composable
fun MissionsScreen(
    uiState: MissionsUiState.HasMissions,
    onClickFeedback: () -> Unit,
    onCheckMission: (mission: Missions.MissionsModel) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = CustomDimensions.padding20)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Complete as missões",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Column {
                Text(
                    text = "Restam",
                    style = MaterialTheme.typography.titleSmall,
                    color = BrowLight,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = uiState.timer ?: "00:00:00",
                    style = MaterialTheme.typography.titleMedium,
                    color = BrowLight,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column {
            LazyColumn(
                modifier = Modifier
                    .padding(vertical = CustomDimensions.padding5)
            ) {
                items(uiState.missions) { mission ->
                    CustomCheckbox(
                        mission = mission,
                        onChangeCheckbox = { onCheckMission(mission) },
                        modifier = Modifier.padding(vertical = CustomDimensions.padding10)
                    )
                }
            }
        }

        CustomIconButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = CustomDimensions.padding20),
            title = "Tem alguma sugestão?",
            icon = Icons.Outlined.Message,
            iconColor = BrowDark,
            onClick = onClickFeedback
        )

        Spacer(
            modifier = Modifier.size(
                height = CustomDimensions.padding20,
                width = CustomDimensions.padding20
            )
        )
    }
}

@Preview
@Composable
fun MissionScreenPreview() {
    val missionsFeed = runBlocking {
        (FakeHomeRepositoryImpl().getRandomMissions() as Result.Success).data
    }
    MissionsScreen(
        uiState = MissionsUiState.HasMissions(
            missions = missionsFeed,
            timer = "20:00:00",
            errorMessages = null,
            selectedMissions = null
        ), onCheckMission = {},
        onClickFeedback = {})
}