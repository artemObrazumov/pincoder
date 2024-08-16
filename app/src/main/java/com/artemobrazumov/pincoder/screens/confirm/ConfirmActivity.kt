package com.artemobrazumov.pincoder.screens.confirm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.artemobrazumov.pincoder.ui.theme.PincoderTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfirmActivity : ComponentActivity() {

    private val viewModel by viewModel<ConfirmViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isSettingNewPin = intent.getBooleanExtra(NEW_PIN, false)
        if (isSettingNewPin) {
            viewModel.setNewPinMode()
        }
        val packageName = intent.getStringExtra(PACKAGE_NAME)
        if (packageName != null) {
            viewModel.setPackageName(packageName)
        }

        enableEdgeToEdge()
        setContent {
            PincoderTheme {
                val state by viewModel.state.collectAsState()
                val finishedActivity by viewModel.activityFinished.collectAsState()
                ConfirmScreen(
                    state = state,
                    onPinCodeEditActionEmitted = { viewModel.onPinCodeEdit(it) }
                )
                BackHandler {}
                if (finishedActivity) {
                    finishAndRemoveTask()
                }
            }
        }
    }

    companion object {
        const val NEW_PIN = "new_pin"
        const val PACKAGE_NAME = "package_name"
    }
}