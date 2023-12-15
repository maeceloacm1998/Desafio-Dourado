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
import androidx.compose.ui.tooling.preview.Preview
import com.app.desafiodourado.commons.mock.challengers
import com.app.desafiodourado.components.image.ImageComponent
import com.app.desafiodourado.components.states.Loading
import com.app.desafiodourado.feature.home.ui.HomeUiState
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.theme.CustomDimensions

@Composable
fun ChallengerListLoading(
    uiState: HomeUiState.HasChallengers,
    onClickItemListener: (challengerItem: Challenger.Card) -> Unit
) {
    val state = rememberLazyGridState()
    val challengerList = uiState.challengers.challengers

    LoadingContent(
        empty = uiState.isChallengerLoading,
        emptyContent = { Loading() },
        content = {
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
    )
}

@Composable
fun ChallengerList(
    uiState: HomeUiState.HasChallengers,
    onClickItemListener: (challengerItem: Challenger.Card) -> Unit
) {
    ChallengerListLoading(
        uiState = uiState,
        onClickItemListener = onClickItemListener
    )
}

@Composable
private fun LoadingContent(
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        content()
    }
}


@Preview
@Composable
fun ChallengerListPreview() {
    ChallengerList(
        uiState = HomeUiState.HasChallengers(
            challengers = Challenger(
                challengers
            ),
            showMissions = true,
            coin = 2000,
            badgeCount = 2,
            finishAllMissions = true,
            selectedChallenger = null,
            isLoading = false,
            isChallengerLoading = false,
            errorMessages = null
        ),
        onClickItemListener = {}
    )
}

@Preview
@Composable
fun ChallengerListLoadingPreview() {
    ChallengerList(
        uiState = HomeUiState.HasChallengers(
            challengers = Challenger(
                challengers
            ),
            showMissions = true,
            coin = 2000,
            badgeCount = 2,
            finishAllMissions = true,
            selectedChallenger = null,
            isLoading = false,
            isChallengerLoading = true,
            errorMessages = null
        ),
        onClickItemListener = {}
    )
}