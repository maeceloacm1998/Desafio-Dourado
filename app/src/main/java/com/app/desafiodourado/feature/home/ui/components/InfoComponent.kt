@file:OptIn(ExperimentalMaterial3Api::class)

package com.app.desafiodourado.feature.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.desafiodourado.components.bottomsheet.BetterModalBottomSheet
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.theme.Background
import com.app.desafiodourado.theme.BrowLight
import com.app.desafiodourado.theme.CustomDimensions
import com.app.desafiodourado.theme.Success

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoComponent(challenger: List<Challenger.Card>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = CustomDimensions.padding5),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var showBottomSheet by rememberSaveable { mutableStateOf(false) }

        InfoCheckChallengers(challenger = challenger)

        IconButton(
            modifier = Modifier
                .clip(CircleShape),
            onClick = { showBottomSheet = true }
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                tint = BrowLight,
                contentDescription = "Info"
            )
        }

        BetterModalBottomSheet(
            showSheet = showBottomSheet,
            onDismissRequest = {
                showBottomSheet = false
            },
            containerColor = Background
        ) {
            InfoBottomSheet()
        }
    }
}

@Composable
fun InfoCheckChallengers(
    modifier: Modifier = Modifier,
    challenger: List<Challenger.Card>
) {
    val filterCompleteChallengers = challenger.filter { it.complete }.size
    val challengersSize = challenger.size
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(start = CustomDimensions.padding10),
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = "check_circle",
            tint = Success
        )
        Text(
            modifier = Modifier.padding(start = CustomDimensions.padding10),
            text = "${filterCompleteChallengers}/${challengersSize}",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            modifier = Modifier.padding(start = CustomDimensions.padding5),
            text = "Desafios completos",
            color = Color.White,
            style = MaterialTheme.typography.titleSmall
        )
    }

}

@Preview
@Composable
fun PreviewInfoComponent() {
    InfoComponent(challenger = listOf())
}

@Preview
@Composable
fun InfoBottomSheet() {
    Column(
        modifier = Modifier.padding(
            end = CustomDimensions.padding20,
            start = CustomDimensions.padding20,
            top = CustomDimensions.padding10,
            bottom = CustomDimensions.padding20
        )
    ) {
        Text(
            modifier = Modifier.padding(bottom = CustomDimensions.padding10),
            text = "Sobre o aplicativo",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Este aplicativo é um jogo interativo onde você pode ganhar prêmios ao coletar moedas. As moedas são obtidas através de missões que são atualizadas diariamente.\n" +
                    "\n" +
                    "Cada missão oferece uma quantidade específica de moedas como recompensa. Se você completar todas as missões do dia, receberá um bônus adicional de moedas.\n" +
                    "\n" +
                    "Existem dois tipos de cartões no jogo: os cartões normais, que têm uma imagem de coruja no centro, e os DESAFIOS DOURADOS, que oferecem prêmios incríveis. Os cartões normais podem ou não conter prêmios, enquanto os cartões de DESAFIO DOURADO sempre contêm algum prêmio. No entanto, para obter esses cartões, você precisará de muitas moedas.",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.size(
                height = CustomDimensions.padding20,
                width = CustomDimensions.padding20
            )
        )
    }
}