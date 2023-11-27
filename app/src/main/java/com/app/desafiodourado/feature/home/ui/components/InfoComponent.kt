package com.app.desafiodourado.feature.home.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.desafiodourado.components.bottomsheet.BetterModalBottomSheet
import com.app.desafiodourado.ui.theme.Background
import com.app.desafiodourado.ui.theme.BrowLight
import com.app.desafiodourado.ui.theme.CustomDimensions

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun InfoComponent() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = CustomDimensions.padding5)
    ) {
        var showBottomSheet by rememberSaveable { mutableStateOf(false) }
        val (imgInfo) = createRefs()

        IconButton(onClick = { showBottomSheet = true }, modifier = Modifier
            .constrainAs(imgInfo) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
            .clip(CircleShape)
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
    }
}