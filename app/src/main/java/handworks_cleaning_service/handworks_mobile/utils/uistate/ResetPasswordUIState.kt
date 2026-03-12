package handworks_cleaning_service.handworks_mobile.utils.uistate

sealed class ResetPasswordUIState {
    object Loading : ResetPasswordUIState()
    object SignedOut : ResetPasswordUIState()
    object NeedsFirstFactor : ResetPasswordUIState()
    object NeedsSecondFactor : ResetPasswordUIState()
    object NeedsNewPassword : ResetPasswordUIState()
    object Complete : ResetPasswordUIState()
}
