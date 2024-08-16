package com.artemobrazumov.pincoder.screens.confirm.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.artemobrazumov.pincoder.screens.confirm.ConfirmScreenState
import com.artemobrazumov.pincoder.screens.confirm.PinCodeState

@Composable
fun PinCodeDotsComponent(
    state: PinCodeState
) {
    val pinCodeLength = state.pinCode.length
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(4) { index ->
            if (pinCodeLength <= index) {
                EmptyDot()
            } else {
                FilledDot()
            }
        }
    }
}

@Composable
fun EmptyDot() {
    Box(
        modifier = Modifier
            .size(36.dp)
            .drawWithContent {
                drawCircle(
                    color = Color.Black,
                    radius = 46f,
                    style = Stroke(8f)
                )
            }
    )
}

@Composable
fun FilledDot() {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    )
}