package com.artemobrazumov.pincoder.screens.main

import android.graphics.Bitmap

data class MainScreenState(
    val pinCodeState: PinCodeState = PinCodeState.Idle,
    val appListState: AppListState = AppListState.Loading,
    val permissionState: PermissionState = PermissionState()
)

sealed class PinCodeState {
    data object Idle: PinCodeState()
    data object Loading: PinCodeState()
    data class Content(
        val isPinCodeSet: Boolean
    ): PinCodeState()
}

sealed class AppListState {
    data object Loading: AppListState()
    data class Content(
        val apps: List<AppInfo>
    ): AppListState()
}

data class PermissionState(
    val isAccessibilityPermissionGranted: Boolean = false,
    val isNotificationPermissionGranted: Boolean = false
)

data class AppInfo(
    val packageId: String,
    val icon: Bitmap,
    val name: String,
    val isSecured: Boolean
)