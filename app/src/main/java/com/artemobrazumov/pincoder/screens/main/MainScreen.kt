package com.artemobrazumov.pincoder.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.artemobrazumov.pincoder.screens.main.components.AppListComponent
import com.artemobrazumov.pincoder.screens.main.components.PermissionDialogComponent
import com.artemobrazumov.pincoder.screens.main.components.PinCodeComponent

@Composable
fun MainScreen(
    state: MainScreenState,
    modifier: Modifier = Modifier,
    onSetNewPinClicked: () -> Unit,
    onSecureStateChanged: (packageId: String, secured: Boolean) -> Unit,
    onNotificationPermissionRequested: () -> Unit,
    onAccessibilityPermissionRequested: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->

        PermissionDialogComponent(
            state = state.permissionState,
            onNotificationPermissionRequested = { onNotificationPermissionRequested() },
            onAccessibilityPermissionRequested = { onAccessibilityPermissionRequested() }
        )

        Column(
            modifier = modifier.padding(innerPadding)
        ) {
            PinCodeComponent(
                state = state.pinCodeState,
                onSetNewPinClicked = { onSetNewPinClicked() }
            )
            AppListComponent(
                state = state.appListState,
                onSecureStateChanged = { packageId: String, secured: Boolean ->
                    onSecureStateChanged(packageId, secured)
                }
            )
        }
    }
}