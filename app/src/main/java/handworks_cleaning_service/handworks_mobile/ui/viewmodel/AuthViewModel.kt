package handworks_cleaning_service.handworks_mobile.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clerk.api.Clerk
import com.clerk.api.signin.SignIn
import com.clerk.api.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import handworks_cleaning_service.handworks_mobile.data.dto.auth.LoginRequest
import handworks_cleaning_service.handworks_mobile.data.repository.AuthRepository
import handworks_cleaning_service.handworks_mobile.data.repository.config.Result
import handworks_cleaning_service.handworks_mobile.data.repository.config.SessionManager
import handworks_cleaning_service.handworks_mobile.utils.uistate.AuthUIState
import handworks_cleaning_service.handworks_mobile.utils.uistate.ResetPasswordUIState
import handworks_cleaning_service.handworks_mobile.utils.uistate.SessionUIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository, private val sessionManager: SessionManager) : ViewModel() {

    private val _authState = MutableLiveData<AuthUIState>(AuthUIState.Idle)
    val authState: LiveData<AuthUIState> get() = _authState

    private val _sessionState = MutableLiveData<SessionUIState>(SessionUIState.Idle)
    val sessionState: LiveData<SessionUIState> get() = _sessionState

    init {
        val cachedUser = repository.getCachedUser()
        if (cachedUser != null) {
            _sessionState.value = SessionUIState.Authenticated(cachedUser)
        } else {
            _sessionState.value = SessionUIState.Idle
        }
    }

    fun clearCache() {
        sessionManager.clearSession()
    }

    private val _resetPasswordUIState = MutableStateFlow<ResetPasswordUIState>(ResetPasswordUIState.Loading)
    val resetPasswordUiState = _resetPasswordUIState.asStateFlow()

    fun signIn(request: LoginRequest) {
        viewModelScope.launch {
            _authState.value = AuthUIState.Loading
            when (val result = repository.signIn(request)) {
                is Result.Success -> _authState.value = AuthUIState.Success(result.data)
                is Result.Failure -> _authState.value = AuthUIState.Error(result.exception.message ?: "Unknown error")
            }
        }
    }

    fun checkSession() {
        viewModelScope.launch {
            _sessionState.value = SessionUIState.Loading

            while (!repository.isClerkInitialized()) {
                delay(200)
            }

            if (!repository.isSignedIn()) {
                _sessionState.value = SessionUIState.Unauthenticated
                return@launch
            }

            val user = repository.getUser()
            _sessionState.value = if (user != null) {
                SessionUIState.Authenticated(user)
            } else {
                SessionUIState.Unauthenticated
            }
        }
    }

    fun getCachedUser(): User? = repository.getCachedUser()

    fun signOut() {
        viewModelScope.launch {
            _authState.value = AuthUIState.Loading
            try {
                repository.signOut()
                _authState.value = AuthUIState.SignedOut
            } catch (e: Exception) {
                _authState.value = AuthUIState.Error(e.message ?: "Logout failed")
            }
        }
    }

    //region Forgot Password/Reset Password
    init {
        viewModelScope.launch {
            combine(
                Clerk.isInitialized,
                Clerk.userFlow
            ) { isInitialized, user ->
                when {
                    !isInitialized -> ResetPasswordUIState.Loading
                    user != null -> ResetPasswordUIState.Complete
                    else -> ResetPasswordUIState.SignedOut
                }
            }.collect { state ->
                _resetPasswordUIState.value = state
            }
        }
    }

    fun createSignIn(email: String) {
        viewModelScope.launch {
            val status = repository.createSignIn(email)
            updateStateFromStatus(status)
        }
    }

    fun verify(code: String) {
        viewModelScope.launch {
            val status = repository.verifyCode(code)
            updateStateFromStatus(status)
        }
    }

    fun setNewPassword(password: String) {
        viewModelScope.launch {
            val status = repository.setNewPassword(password)
            updateStateFromStatus(status)
        }
    }

    private fun updateStateFromStatus(status: SignIn.Status) {
        Log.d("ResetPassword", "updateStateFromStatus: $status")
        _resetPasswordUIState.value = when (status) {
            SignIn.Status.NEEDS_FIRST_FACTOR -> ResetPasswordUIState.NeedsFirstFactor
            SignIn.Status.NEEDS_SECOND_FACTOR -> ResetPasswordUIState.NeedsSecondFactor
            SignIn.Status.NEEDS_NEW_PASSWORD -> ResetPasswordUIState.NeedsNewPassword
            SignIn.Status.COMPLETE -> ResetPasswordUIState.Complete
            else -> ResetPasswordUIState.SignedOut
        }
    }
    //endregion
}