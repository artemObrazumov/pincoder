package com.artemobrazumov.pincoder.screens.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artemobrazumov.pincoder.screens.main.AppInfo
import com.artemobrazumov.pincoder.screens.main.AppListState

@Composable
fun AppListComponent(
    state: AppListState,
    onSecureStateChanged: (packageId: String, secured: Boolean) -> Unit
) {
    when(state) {
        is AppListState.Content -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = state.apps) { app ->
                    AppInfoItem(
                        appInfo = app,
                        onSecureStateChanged = { onSecureStateChanged(app.packageId, it) }
                    )
                }
            }
        }

        AppListState.Loading -> {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun AppInfoItem(
    appInfo: AppInfo,
    onSecureStateChanged: (secured: Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(48.dp),
            bitmap = appInfo.icon.asImageBitmap(),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = appInfo.name,
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp
            ),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = appInfo.isSecured,
            onCheckedChange = { onSecureStateChanged(!appInfo.isSecured) }
        )
    }
}