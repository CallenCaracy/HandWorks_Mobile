package handworks_cleaning_service.handworks_mobile.utils.uistate

sealed class ResetPasswordUiState {
    object Loading : ResetPasswordUiState()
    object SignedOut : ResetPasswordUiState()
    object NeedsFirstFactor : ResetPasswordUiState()
    object NeedsSecondFactor : ResetPasswordUiState()
    object NeedsNewPassword : ResetPasswordUiState()
    object Complete : ResetPasswordUiState()
}
