package com.artemobrazumov.pincoder.screens.confirm

sealed class PinCodeEditAction{
    data object Delete: PinCodeEditAction()
    data class Number(
        val number: Char
    ): PinCodeEditAction()
}
