package handworks_cleaning_service.handworks_mobile.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clerk.api.session.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import handworks_cleaning_service.handworks_mobile.data.dto.LoginRequest
import handworks_cleaning_service.handworks_mobile.data.repository.AuthRepository
import handworks_cleaning_service.handworks_mobile.utils.Result
import handworks_cleaning_service.handworks_mobile.utils.uistate.AuthUiState
import handworks_cleaning_service.handworks_mobile.utils.Constant.DELAY_MILLIS
import handworks_cleaning_service.handworks_mobile.utils.Constant.MAX_RETRIES
import handworks_cleaning_service.handworks_mobile.utils.uistate.SessionUiState
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private val _authState = MutableLiveData<AuthUiState>()
    val authState: LiveData<AuthUiState> get() = _authState

    private val _sessionState = MutableLiveData<SessionUiState>()
    val sessionState: LiveData<SessionUiState> get() = _sessionState

    fun signIn(request: LoginRequest) {
        viewModelScope.launch {
            _authState.value = AuthUiState.Loading
            when (val result = repository.signIn(request)) {
                is Result.Success -> _authState.value = AuthUiState.Success(result.data)
                is Result.Failure -> _authState.value = AuthUiState.Error(result.exception.message ?: "Unknown error")
            }
        }
    }

    fun waitForSessionReady() {
        viewModelScope.launch {
            _sessionState.value = SessionUiState.Loading
            val session = pollSessionWithRetry()
            if (session != null) {
                _sessionState.value = SessionUiState.Ready(session)
            } else {
                _sessionState.value = SessionUiState.Error("Session not available")
            }
        }
    }

    private suspend fun pollSessionWithRetry(): Session? {
        repeat(MAX_RETRIES) { _ ->
            val session = repository.getSession()
            if (session != null) return session
            delay(DELAY_MILLIS)
        }
        return null
    }

    fun signOut() {
        viewModelScope.launch {
            _authState.value = AuthUiState.Loading
            try {
                repository.signOut()
                _authState.value = AuthUiState.SignedOut
            } catch (e: Exception) {
                _authState.value = AuthUiState.Error(e.message ?: "Logout failed")
            }
        }
    }
}