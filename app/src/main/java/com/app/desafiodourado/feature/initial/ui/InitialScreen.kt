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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.app.desafiodourado.R
import com.app.desafiodourado.components.background.Background
import com.app.desafiodourado.components.snackbar.SnackbarCustomType
import com.app.desafiodourado.components.textfield.TextFieldCustom
import com.app.desafiodourado.core.routes.Routes
import com.app.desafiodourado.core.utils.UiState
import com.app.desafiodourado.ui.theme.BackgroundTransparent
import com.app.desafiodourado.ui.theme.BrowLight
import com.app.desafiodourado.ui.theme.CustomDimensions
import org.koin.androidx.compose.koinViewModel

@Composable
fun InitialScreen(navController: NavController, viewModel: InitialViewModel = koinViewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    var nameState by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    val userNameError by viewModel.userNameError.observeAsState(initial = false)
    val snackbarType by viewModel.snackbarType.observeAsState(initial = SnackbarCustomType.SUCCESS)

    Observables(
        snackbarHostState = snackbarHostState,
        navController = navController,
        viewModel = viewModel,
        onLoading = {
            isLoading = it
        })

    InitialComponents(
        snackbarHostState = snackbarHostState,
        isLoading = isLoading,
        userNameError = userNameError,
        snackbarType = snackbarType,
        onNameState = { name -> nameState = name },
        onSubmitButton = { viewModel.createUser(nameState) }
    )
}

@Composable
fun Observables(
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    viewModel: InitialViewModel,
    onLoading: (state: Boolean) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

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
            viewModel.showSnackBar(
                snackbarHostState = snackbarHostState,
                message = stringResource(id = R.string.initial_screen_snackbar_error),
                snackbarType = SnackbarCustomType.ERROR
            )
        }

        else -> Unit
    }
}

@Composable
fun InitialComponents(
    snackbarHostState: SnackbarHostState,
    isLoading: Boolean,
    userNameError: Boolean,
    snackbarType: SnackbarCustomType,
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

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(CustomDimensions.padding50)
                .padding(horizontal = CustomDimensions.padding20)
                .constrainAs(btGoToAdventure) {
                    top.linkTo(tfName.bottom)
                    bottom.linkTo(parent.bottom)
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = BrowLight,
                disabledContainerColor = BackgroundTransparent
            ),
            enabled = !isLoading,
            onClick = { onSubmitButton() },
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White, modifier = Modifier.size(
                        width = CustomDimensions.padding20,
                        height = CustomDimensions.padding20
                    )
                )
            } else {
                Text(
                    text = stringResource(id = R.string.initial_screen_bt_label),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun InitialScreenPreview() {
    val fakeSnackbarHostState = remember { SnackbarHostState() }

    InitialComponents(
        snackbarHostState = fakeSnackbarHostState,
        isLoading = false,
        userNameError = false,
        snackbarType = SnackbarCustomType.ERROR,
        onSubmitButton = {},
        onNameState = {}
    )
}