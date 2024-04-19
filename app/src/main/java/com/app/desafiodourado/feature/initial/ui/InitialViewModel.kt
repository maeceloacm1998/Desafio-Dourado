package com.app.desafiodourado.feature.initial.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.app.desafiodourado.core.routes.Routes
import com.app.desafiodourado.feature.initial.domain.CreateChallengersUseCase
import com.app.desafiodourado.feature.initial.domain.CreateMissionsUseCase
import com.app.desafiodourado.feature.initial.domain.CreateUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InitialViewModel(
    private val createUserUseCase: CreateUserUseCase,
    private val createChallengersUseCase: CreateChallengersUseCase,
    private val createMissionsUseCase: CreateMissionsUseCase
) : ViewModel() {
    private val viewModelState = MutableStateFlow(InitialViewModelState())

    val uiState = viewModelState
        .map(InitialViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        viewModelScope.launch {
            handleChallengerScheme()
            handleMissionScheme()
        }
    }

    fun createUser(userName: String, navController: NavController) {
        val userNameIsEmpty = userName.isEmpty()
        onSetLoading(true)

        if (userNameIsEmpty) {
            onSetError(true)
            onSetLoading(false)
        } else {
            handleUserScheme(
                userName = userName,
                navController = navController
            )
        }
    }

    private fun handleUserScheme(userName: String, navController: NavController) {
        viewModelScope.launch {
            createUserUseCase(userName)
                .onSuccess {
                    goToHome(navController)
                }
                .onFailure {
                    onSetError(true)
                    onSetLoading(false)
                }
        }
    }

    private suspend fun handleMissionScheme() {
        createMissionsUseCase()
    }

    private suspend fun handleChallengerScheme() {
        createChallengersUseCase()
    }

    private fun goToHome(navController: NavController) {
        navController.navigate(Routes.Home.route) {
            popUpTo(Routes.Initial.route) {
                inclusive = true
            }
        }
    }

    private fun onSetLoading(state: Boolean) {
        viewModelState.update { it.copy(isLoading = state) }
    }

    private fun onSetError(state: Boolean) {
        viewModelState.update { it.copy(isUserNameError = state) }
    }
}