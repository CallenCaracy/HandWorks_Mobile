package handworks_cleaning_service.handworks_mobile.utils.uistate

import com.clerk.api.session.Session

sealed class SessionUiState {
    object Loading : SessionUiState()
    data class Ready(val session: Session) : SessionUiState()
    data class Error(val message: String) : SessionUiState()
}