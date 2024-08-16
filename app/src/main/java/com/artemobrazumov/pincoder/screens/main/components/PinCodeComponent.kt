package com.artemobrazumov.pincoder.screens.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.artemobrazumov.pincoder.screens.main.PinCodeState

@Composable
fun PinCodeComponent(
    state: PinCodeState,
    modifier: Modifier = Modifier,
    onSetNewPinClicked: () -> Unit
) {
    when(state) {
        PinCodeState.Loading -> {

        }
        is PinCodeState.Content -> {
            Column(
                modifier = modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = "Состояние пин-кода"
                )
                Text(
                    text = if (state.isPinCodeSet) "Введен" else "Отсутствует",
                    color = if (state.isPinCodeSet) Color.Green else Color.Red
                )
                Button(onClick = { onSetNewPinClicked() }) {
                    Text(text = if (state.isPinCodeSet) "Обновить" else "Задать")
                }
            }
        }

        PinCodeState.Idle -> {}
    }
}