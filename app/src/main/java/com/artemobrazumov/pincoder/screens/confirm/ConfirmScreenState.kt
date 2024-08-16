package com.artemobrazumov.pincoder.screens.confirm

data class ConfirmScreenState(
    val pinCodeState: PinCodeState = PinCodeState(),
    val pinCodeInputEnabled: Boolean = true,
    val isSettingNewPinCode: Boolean = false,
    val pinCodeUpdateState: PinCodeUpdateState = PinCodeUpdateState.Idle,
    val pinCodeVerificationState: PinCodeVerificationState = PinCodeVerificationState.Idle,
)

data class PinCodeState(
    val pinCode: String = ""
)

sealed class PinCodeUpdateState {
    data object Idle: PinCodeUpdateState()
    data object Updating: PinCodeUpdateState()
    data object Success: PinCodeUpdateState()
    data class Error(
        val errorMessage: String
    ): PinCodeUpdateState()
}

sealed class PinCodeVerificationState {
    data object Idle: PinCodeVerificationState()
    data object Incorrect: PinCodeVerificationState()
    data object Correct: PinCodeVerificationState()
}