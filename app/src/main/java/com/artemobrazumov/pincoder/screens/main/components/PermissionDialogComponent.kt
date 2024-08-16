package com.artemobrazumov.pincoder.screens.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.artemobrazumov.pincoder.screens.main.PermissionState

@Composable
fun PermissionDialogComponent(
    state: PermissionState,
    onNotificationPermissionRequested: () -> Unit,
    onAccessibilityPermissionRequested: () -> Unit,
) {
    if (!state.isNotificationPermissionGranted || !state.isAccessibilityPermissionGranted) {
        Dialog(
            onDismissRequest = {}
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Необходимы разрешения",
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                    if (!state.isNotificationPermissionGranted) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = { onNotificationPermissionRequested() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Уведомления")
                        }
                    }
                    if (!state.isAccessibilityPermissionGranted) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = { onAccessibilityPermissionRequested() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Спец. возможности")
                        }
                    }
                }
            }
        }
    }
}