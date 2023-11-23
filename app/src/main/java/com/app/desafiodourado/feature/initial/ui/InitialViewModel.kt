package com.app.desafiodourado.feature.initial.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.desafiodourado.core.utils.UiState
import com.app.desafiodourado.feature.initial.domain.CreateUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InitialViewModel(private val createUserUseCase: CreateUserUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<Boolean>>(UiState.Success(false))
    val uiState: StateFlow<UiState<Boolean>> = _uiState

    private val _userNameError = MutableLiveData<Boolean>()
    val userNameError: LiveData<Boolean> = _userNameError

    fun createUser(userName: String) {
        viewModelScope.launch {
            if (userName.isNotEmpty()) {
                _uiState.value = UiState.Loading
                setUserNameError(false)

                createUserUseCase(userName)
                    .onSuccess {
                        _uiState.value = UiState.Success(it)
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