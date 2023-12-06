package com.app.desafiodourado.feature.initial.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.desafiodourado.core.utils.UiState
import com.app.desafiodourado.feature.initial.domain.CreateChallengersUseCase
import com.app.desafiodourado.feature.initial.domain.CreateMissionsUseCase
import com.app.desafiodourado.feature.initial.domain.CreateUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        viewModelScope.launch {
            val job1 = async { createChallengersUseCase() }
            val job2 = async { createMissionsUseCase() }

            job1.await()
            job2.await()

            withContext(Dispatchers.Main) {
                createUser(userName)
            }
        }
    }

    private suspend fun createUser(userName: String) {
        if (userName.isNotEmpty()) {
            _uiState.value = UiState.Loading
            setUserNameError(false)

            createUserUseCase(userName)
                .onSuccess {
                    _uiState.value = UiState.Success(true)
                }.onFailure {
                    _uiState.value = UiState.Error(it)
                }
        } else {
            setUserNameError(true)
        }
    }

    private fun setUserNameError(value: Boolean) {
        _userNameError.value = value
    }
}