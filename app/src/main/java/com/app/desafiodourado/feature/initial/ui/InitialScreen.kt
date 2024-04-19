package com.app.desafiodourado.feature.initial.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.desafiodourado.R
import com.app.desafiodourado.components.button.NormalButton
import com.app.desafiodourado.components.textfield.TextFieldCustom
import com.app.desafiodourado.theme.CustomDimensions

@Composable
fun InitialScreen(
    uiState: InitialViewModelUiState.Data,
    onSubmitButton: (userName: String) -> Unit,
) {
    var nameState by rememberSaveable { mutableStateOf("") }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (imgChallenger, tfName, btGoToAdventure) = createRefs()
        val image = painterResource(id = R.drawable.challenger)

        createVerticalChain(
            imgChallenger, tfName, btGoToAdventure, chainStyle = ChainStyle.Packed
        )

        Image(
            painter = image,
            contentDescription = "Background Image",
            modifier = Modifier
                .size(
                    height = CustomDimensions.padding150,
                    width = CustomDimensions.padding150
                )
                .constrainAs(imgChallenger) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(tfName.top)
                    start.linkTo(parent.start)
                },
            contentScale = ContentScale.Crop,
        )

        TextFieldCustom(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = CustomDimensions.padding20,
                    end = CustomDimensions.padding20,
                    top = CustomDimensions.padding50,
                    bottom = CustomDimensions.padding10
                )
                .constrainAs(tfName) {
                    top.linkTo(imgChallenger.bottom)
                    bottom.linkTo(btGoToAdventure.top)
                },
            label = stringResource(id = R.string.initial_screen_tf_label),
            onChangeListener = {
                nameState = it
            },
            error = uiState.isUserNameError,
            supportText = if (uiState.isUserNameError) stringResource(id = R.string.initial_screen_tf_error) else ""
        )


        NormalButton(
            title = stringResource(id = R.string.initial_screen_bt_label),
            onButtonListener = { onSubmitButton(nameState) },
            loading = uiState.isLoading,
            modifier = Modifier
                .padding(horizontal = CustomDimensions.padding20)
                .constrainAs(btGoToAdventure) {
                    top.linkTo(tfName.bottom)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

@Preview
@Composable
fun InitialScreenPreview() {
    InitialScreen(
        uiState = InitialViewModelUiState.Data(
            isLoading = false,
            isUserNameError = false
        ),
        onSubmitButton = {}
    )
}