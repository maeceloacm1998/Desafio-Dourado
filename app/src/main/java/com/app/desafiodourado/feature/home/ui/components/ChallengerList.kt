package com.app.desafiodourado.feature.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.app.desafiodourado.commons.mock.challengers
import com.app.desafiodourado.components.image.ImageComponent
import com.app.desafiodourado.feature.home.ui.HomeUiState
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.theme.CustomDimensions

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
fun ChallengerListLoading(
    uiState: HomeUiState.HasChallengers,
    listState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    onClickItemListener: (challengerItem: Challenger.Card) -> Unit
) {
    LoadingContent(
        isEmpty = uiState.challengers.challengers.isEmpty(),
        emptyContent = { ChallengerListError() },
        content = {
            ChallengerGrid(
                uiState = uiState,
                listState = listState,
                onClickItemListener = onClickItemListener
            )
        }
    )
}

@Composable
fun ChallengerGrid(
    uiState: HomeUiState.HasChallengers,
    listState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    onClickItemListener: (challengerItem: Challenger.Card) -> Unit
) {
    val challengerList = uiState.challengers.challengers

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(3),
        contentPadding = PaddingValues(CustomDimensions.padding10),
        horizontalArrangement = Arrangement.spacedBy(CustomDimensions.padding10),
        verticalItemSpacing = CustomDimensions.padding5,
        state = listState
    ) {
        items(challengerList.size) { index ->
            val challengerItem = challengerList[index]
            ChallengerItem(
                challengerItem = challengerItem,
                onClickItemListener = onClickItemListener
            )
        }
    }
}

@Composable
fun ChallengerItem(
    challengerItem: Challenger.Card,
    onClickItemListener: (challengerItem: Challenger.Card) -> Unit
) {
    ImageComponent(
        url = if (challengerItem.complete) challengerItem.completeImage else challengerItem.image,
        modifier = Modifier
            .size(
                width = CustomDimensions.padding160,
                height = CustomDimensions.padding160
            ),
        onCLickImageListener = { onClickItemListener(challengerItem) },
        contentScale = ContentScale.Inside
    )
}

@Composable
private fun LoadingContent(
    isEmpty: Boolean,
    emptyContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    when {
        isEmpty -> emptyContent()
        else -> content()
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
            showMissions = false,
            coin = 2000,
            badgeCount = 2,
            finishAllMissions = false,
            selectedChallenger = null,
            isLoading = false,
            isRefresh = false,
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
            isRefresh = true,
            errorMessages = null
        ),
        onClickItemListener = {}
    )
}