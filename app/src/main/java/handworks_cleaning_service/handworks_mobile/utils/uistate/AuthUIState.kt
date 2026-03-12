package handworks_cleaning_service.handworks_mobile.utils.uistate

import com.clerk.api.signin.SignIn

sealed class AuthUIState {
    object Idle : AuthUIState()
    object Loading : AuthUIState()
    data class Success(val data: SignIn) : AuthUIState()
    data class Error(val message: String) : AuthUIState()
    object SignedOut : AuthUIState()
}