package handworks_cleaning_service.handworks_mobile.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import handworks_cleaning_service.handworks_mobile.data.dto.LoginRequest
import handworks_cleaning_service.handworks_mobile.data.repository.AuthRepository
import handworks_cleaning_service.handworks_mobile.utils.Result
import handworks_cleaning_service.handworks_mobile.utils.SignInUiState
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private val _uiState = MutableLiveData<SignInUiState>()
    val uiState: LiveData<SignInUiState> get() = _uiState

    fun signIn(request: LoginRequest) {
        viewModelScope.launch {
            _uiState.value = SignInUiState.Loading
            when (val result = repository.signIn(request)) {
                is Result.Success -> _uiState.value = SignInUiState.Success(result.data)
                is Result.Failure -> _uiState.value = SignInUiState.Error(result.exception.message ?: "Unknown error")
            }
        }
    }
}