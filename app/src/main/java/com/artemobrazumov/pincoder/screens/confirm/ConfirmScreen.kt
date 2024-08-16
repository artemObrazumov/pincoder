package com.artemobrazumov.pincoder.screens.confirm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artemobrazumov.pincoder.screens.confirm.components.PinCodeDotsComponent
import com.artemobrazumov.pincoder.screens.confirm.components.PinCodePanelComponent

@Composable
fun ConfirmScreen(
    state: ConfirmScreenState,
    onPinCodeEditActionEmitted: (action: PinCodeEditAction) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(0.8f)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (state.isSettingNewPinCode) "Установить код"
                    else "Введите код",
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 24.sp
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                PinCodeDotsComponent(
                    state = state.pinCodeState
                )
                Spacer(modifier = Modifier.height(16.dp))
                PinCodePanelComponent(
                    enabled = state.pinCodeInputEnabled,
                    onPinCodeEditActionEmitted = { onPinCodeEditActionEmitted(it) }
                )
                if (state.pinCodeVerificationState is PinCodeVerificationState.Incorrect) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Неправильный пин-код"
                    )
                }
                if (state.pinCodeUpdateState is PinCodeUpdateState.Error) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Ошибка при сохранении пин-кода"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ConfirmScreenPreview() {
    ConfirmScreen(state = ConfirmScreenState(
        pinCodeState = PinCodeState("324")
    )) {

    }
}