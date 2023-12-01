package com.app.desafiodourado.feature.home.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.desafiodourado.components.image.ImageComponent
import com.app.desafiodourado.feature.home.ui.model.Challenger.Card
import com.app.desafiodourado.ui.theme.CustomDimensions

@Composable
fun ChallengerList(
    challengerList: List<Card>,
    onClickItemListener: (challenger: Card) -> Unit
) {
    val state = rememberLazyGridState()

    if (challengerList.isEmpty()) {
        ChallengerListError()
    } else {
        LazyVerticalGrid(
            state = state,
            columns = GridCells.Fixed(count = 3)
        ) {
            items(challengerList) { item ->
                ImageComponent(
                    modifier = Modifier
                        .size(
                            width = CustomDimensions.padding160,
                            height = CustomDimensions.padding160
                        )
                        .padding(
                            vertical = CustomDimensions.padding2
                        ),
                    url = item.image,
                    onCLickImageListener = {
                        onClickItemListener(item)
                    }
                )
            }
        }
    }
}