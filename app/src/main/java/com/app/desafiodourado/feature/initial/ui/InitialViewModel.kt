package com.app.desafiodourado.feature.initial.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.desafiodourado.core.utils.UiState
import com.app.desafiodourado.feature.initial.data.InitialRepository
import com.app.desafiodourado.feature.initial.domain.CreateChallengersUseCase
import com.app.desafiodourado.feature.initial.domain.CreateMissionsUseCase
import com.app.desafiodourado.feature.initial.domain.CreateUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class InitialViewModel(
    private val createUserUseCase: CreateUserUseCase,
    private val createChallengersUseCase: CreateChallengersUseCase,
    private val createMissionsUseCase: CreateMissionsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<Boolean>?>(null)
    val uiState: StateFlow<UiState<Boolean>?> = _uiState

    private val _userNameError = MutableLiveData<Boolean>()
    val userNameError: LiveData<Boolean> = _userNameError

    fun init(userName: String) {
        runBlocking {
            createChallengersUseCase()
            createMissionsUseCase()
        }
        createUser(userName)
    }

    private fun createUser(userName: String) {
        viewModelScope.launch {
            if (userName.isNotEmpty()) {
                _uiState.value = UiState.Loading
                setUserNameError(false)

                createUserUseCase(userName)
                    .onSuccess { success ->
                        _uiState.value = UiState.Success(true)
                    }.onFailure {
                        _uiState.value = UiState.Error(it)
                    }
            } else {
                setUserNameError(true)
            }
        }
    }

    private fun setUserNameError(value: Boolean) {
        _userNameError.value = value
    }
}