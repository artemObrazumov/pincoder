package com.artemobrazumov.pincoder.screens.main

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.artemobrazumov.pincoder.screens.confirm.ConfirmActivity
import com.artemobrazumov.pincoder.service.PinAccessibilityService
import com.artemobrazumov.pincoder.ui.theme.PincoderTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        checkForPermissions()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStart() {
        checkForPermissions()
        super.onStart()
    }

    private fun checkForPermissions() {
        val isAccessibilityGranted = isAccessibilityServiceEnabled()
        viewModel.updateAccessibilityPermission(isAccessibilityGranted)

        val isNotificationGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this@MainActivity,
                POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
        viewModel.updateNotificationPermission(isNotificationGranted)
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        var accessibilityEnabled = 0
        val service: String = packageName + "/" + PinAccessibilityService::class.java.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                applicationContext.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED,
            )
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue: String = Settings.Secure.getString(
                applicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
            )
            mStringColonSplitter.setString(settingValue)
            while (mStringColonSplitter.hasNext()) {
                val accessibilityService = mStringColonSplitter.next()

                if (accessibilityService.equals(service, ignoreCase = true)) {
                    return true
                }
            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val queryIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val appsList = packageManager.queryIntentActivities(
            queryIntent, PackageManager.MATCH_ALL
        ).distinctBy { it.activityInfo.packageName }
        viewModel.updateAppsList(appsList, packageManager)

        enableEdgeToEdge()
        setContent {
            PincoderTheme {
                val state by viewModel.state.collectAsState()
                MainScreen(
                    state = state,
                    onSetNewPinClicked = {
                        startActivity(
                            Intent(this, ConfirmActivity::class.java).apply {
                                putExtra(ConfirmActivity.NEW_PIN, true)
                            }
                        )
                    },
                    onSecureStateChanged = { packageId: String, secured: Boolean ->
                        viewModel.updateSecureState(packageId, secured)
                    },
                    onNotificationPermissionRequested = {
                        requestNotificationPermission()
                    },
                    onAccessibilityPermissionRequested = {
                        requestAccessibilityPermission()
                    }
                )
            }
        }
    }

    private fun requestAccessibilityPermission() {
        val openSettingsIntent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
        }
        startActivity(openSettingsIntent)
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

        ActivityCompat.requestPermissions(
            this, arrayOf(POST_NOTIFICATIONS), 1
        )
    }
}