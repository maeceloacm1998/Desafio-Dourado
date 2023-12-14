package com.app.desafiodourado.feature.initial.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.app.desafiodourado.R
import com.app.desafiodourado.components.button.NormalButton
import com.app.desafiodourado.components.textfield.TextFieldCustom
import com.app.desafiodourado.core.routes.Routes
import com.app.desafiodourado.core.utils.UiState
import com.app.desafiodourado.theme.CustomDimensions
import org.koin.androidx.compose.koinViewModel

@Composable
fun InitialScreen(navController: NavController, viewModel: InitialViewModel = koinViewModel()) {
    var nameState by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    val userNameError by viewModel.userNameError.observeAsState(initial = false)

    Observables(
        navController = navController,
        viewModel = viewModel,
        onLoading = {
            isLoading = it
        })

    InitialComponents(
        isLoading = isLoading,
        userNameError = userNameError,
        onNameState = { name -> nameState = name },
        onSubmitButton = { viewModel.init(nameState) }
    )
}

@Composable
fun Observables(
    navController: NavController,
    viewModel: InitialViewModel,
    onLoading: (state: Boolean) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is UiState.Loading -> {
            onLoading(true)
        }

        is UiState.Success -> {
            navController.navigate(Routes.Home.route) {
                popUpTo(Routes.Initial.route) {
                    inclusive = true
                }
            }
        }

        is UiState.Error -> {
            onLoading(false)
        }

        else -> Unit
    }
}

@Composable
fun InitialComponents(
    isLoading: Boolean,
    userNameError: Boolean,
    onSubmitButton: () -> Unit,
    onNameState: (name: String) -> Unit
) {

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
                onNameState(it)
            },
            error = userNameError,
            supportText = if (userNameError) stringResource(id = R.string.initial_screen_tf_error) else ""
        )


        NormalButton(
            title = stringResource(id = R.string.initial_screen_bt_label),
            onButtonListener = { onSubmitButton() },
            loading = isLoading,
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
    InitialComponents(
        isLoading = false,
        userNameError = false,
        onSubmitButton = {},
        onNameState = {}
    )
}