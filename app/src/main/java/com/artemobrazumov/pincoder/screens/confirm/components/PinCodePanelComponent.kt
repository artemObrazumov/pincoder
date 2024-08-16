package com.artemobrazumov.pincoder.screens.confirm.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artemobrazumov.pincoder.screens.confirm.PinCodeEditAction

@Composable
fun PinCodePanelComponent(
    enabled: Boolean,
    onPinCodeEditActionEmitted: (action: PinCodeEditAction) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = "123456789 0D".toList()) { item ->
            if (item.isDigit()) {
                PanelItem(
                    enabled = enabled,
                    onClick = { onPinCodeEditActionEmitted(PinCodeEditAction.Number(item)) }
                ) {
                    NumberBlock(
                        number = item
                    )
                }
            } else if (item == 'D') {
                PanelItem(
                    enabled = enabled,
                    onClick = { onPinCodeEditActionEmitted(PinCodeEditAction.Delete) }
                ) {
                    Text(
                        text = "Del",
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun PanelItem(
    enabled: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .clickable { onClick() }
            .alpha(if (enabled) 1f else 0.5f)
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun NumberBlock(
    number: Char
) {
    Text(
        text = number.toString(),
        style = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 28.sp
        ),
        color = MaterialTheme.colorScheme.primary
    )
}