package handworks_cleaning_service.handworks_mobile.ui.viewmodel

import androidx.compose.runtime.internal.updateLiveLiteralValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clerk.api.Clerk
import com.clerk.api.session.Session
import com.clerk.api.signin.SignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import handworks_cleaning_service.handworks_mobile.data.dto.LoginRequest
import handworks_cleaning_service.handworks_mobile.data.repository.AuthRepository
import handworks_cleaning_service.handworks_mobile.utils.Result
import handworks_cleaning_service.handworks_mobile.utils.uistate.AuthUiState
import handworks_cleaning_service.handworks_mobile.utils.Constant.DELAY_MILLIS
import handworks_cleaning_service.handworks_mobile.utils.Constant.MAX_RETRIES
import handworks_cleaning_service.handworks_mobile.utils.uistate.ResetPasswordUiState
import handworks_cleaning_service.handworks_mobile.utils.uistate.SessionUiState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private val _authState = MutableLiveData<AuthUiState>()
    val authState: LiveData<AuthUiState> get() = _authState

    private val _sessionState = MutableLiveData<SessionUiState>()
    val sessionState: LiveData<SessionUiState> get() = _sessionState

    private val _resetPasswordUiState = MutableStateFlow<ResetPasswordUiState>(ResetPasswordUiState.Loading)
    val resetPasswordUiState = _resetPasswordUiState.asStateFlow()

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

    //region Forgot Password/Reset Password
    init {
        combine(Clerk.isInitialized, Clerk.userFlow) { isInitialized, user ->
            _resetPasswordUiState.value = when {
                !isInitialized -> ResetPasswordUiState.Loading
                user != null -> ResetPasswordUiState.Complete
                else -> ResetPasswordUiState.SignedOut
            }
        }
    }

    fun createSignIn(email: String) {
        viewModelScope.launch {
            repository.createSignIn(email) { updateStateFromStatus(it) }
        }
    }

    fun verify(code: String) {
        viewModelScope.launch {
            repository.verifyCode(code) { updateStateFromStatus(it) }
        }
    }

    fun setNewPassword(newPassword: String) {
        viewModelScope.launch {
            repository.setNewPassword(newPassword) { updateStateFromStatus(it) }
        }
    }

    private fun updateStateFromStatus(status: SignIn.Status) {
        _resetPasswordUiState.value = when (status) {
            SignIn.Status.COMPLETE -> ResetPasswordUiState.Complete
            SignIn.Status.NEEDS_FIRST_FACTOR -> ResetPasswordUiState.NeedsFirstFactor
            SignIn.Status.NEEDS_SECOND_FACTOR -> ResetPasswordUiState.NeedsSecondFactor
            SignIn.Status.NEEDS_NEW_PASSWORD -> ResetPasswordUiState.NeedsNewPassword
            else -> ResetPasswordUiState.SignedOut
        }
    }
    //endregion
}