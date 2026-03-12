package handworks_cleaning_service.handworks_mobile.utils.uistate

import com.clerk.api.user.User

sealed class SessionUIState {
    object Idle : SessionUIState()
    object Loading : SessionUIState()
    data class Authenticated(val user: User?) : SessionUIState()
    object Unauthenticated : SessionUIState()
    data class Error(val message: String) : SessionUIState()
}