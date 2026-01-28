package handworks_cleaning_service.handworks_mobile.utils.uistate

import com.clerk.api.user.User

sealed class SessionUiState {
    object Idle : SessionUiState()
    object Loading : SessionUiState()
    data class Authenticated(val user: User?) : SessionUiState()
    object Unauthenticated : SessionUiState()
    data class Error(val message: String) : SessionUiState()
}