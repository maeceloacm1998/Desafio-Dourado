package com.app.desafiodourado.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.app.desafiodourado.R
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.ui.theme.CustomDimensions

@Composable
fun ChallengerList(
    challengerList: List<Challenger.Card>,
    onClickItemListener: (challengerItem: Challenger.Card) -> Unit
) {
    val image = painterResource(id = R.drawable.default_redux)
    val imageGold = painterResource(id = R.drawable.golld_challenger_redux)

    val state = rememberLazyGridState()

    if (challengerList.isEmpty()) {
        ChallengerListError()
    } else {
        LazyVerticalGrid(
            state = state,
            columns = GridCells.Fixed(count = 3),
        ) {
            items(challengerList) { item ->
                
                Image(
                    painter = if (item.type == "NORMAL") image else imageGold,
                    contentDescription = "Background Image",
                    modifier = Modifier
                        .size(
                            width = CustomDimensions.padding160,
                            height = CustomDimensions.padding160
                        )
                        .padding(
                            vertical = CustomDimensions.padding2
                        )
                        .clickable { onClickItemListener(item) },
                )
            }
        }
    }
}