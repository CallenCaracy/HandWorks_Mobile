package handworks_cleaning_service.handworks_mobile.utils.uistate

import com.clerk.api.signin.SignIn

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val data: SignIn) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
    object SignedOut : AuthUiState()
}