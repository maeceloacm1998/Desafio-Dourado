package com.app.desafiodourado.feature.home.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.app.desafiodourado.components.image.ImageComponent
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.theme.CustomDimensions

@Composable
fun ChallengerList(
    challengerList: List<Challenger.Card>,
    onClickItemListener: (challengerItem: Challenger.Card) -> Unit
) {
    val state = rememberLazyGridState()

    if (challengerList.isEmpty()) {
        ChallengerListError()
    } else {
        LazyVerticalGrid(
            state = state,
            columns = GridCells.Fixed(count = 3),
        ) {
            items(challengerList) { item ->
                ImageComponent(
                    url = if (item.complete) item.completeImage else item.image,
                    modifier = Modifier
                        .size(
                            width = CustomDimensions.padding160,
                            height = CustomDimensions.padding160
                        )
                        .padding(
                            vertical = CustomDimensions.padding2
                        ),
                    onCLickImageListener = { onClickItemListener(item) },
                    contentScale = ContentScale.Inside
                )
            }
        }
    }
}