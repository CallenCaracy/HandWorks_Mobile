package handworks_cleaning_service.handworks_mobile.utils

import com.clerk.api.signin.SignIn

sealed class SignInUiState {
    object Loading : SignInUiState()
    data class Success(val data: SignIn) : SignInUiState()
    data class Error(val message: String) : SignInUiState()
}
