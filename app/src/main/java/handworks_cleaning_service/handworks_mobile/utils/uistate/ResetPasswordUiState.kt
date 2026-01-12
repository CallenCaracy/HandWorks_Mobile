package handworks_cleaning_service.handworks_mobile.utils.uistate

sealed interface ResetPasswordUiState {
    data object Loading : ResetPasswordUiState
    data object SignedOut : ResetPasswordUiState
    data object NeedsFirstFactor : ResetPasswordUiState
    data object NeedsSecondFactor : ResetPasswordUiState
    data object NeedsNewPassword : ResetPasswordUiState
    data object Complete : ResetPasswordUiState
}