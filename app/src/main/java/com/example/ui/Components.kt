package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Asphalt
import com.example.ui.theme.BorderGrey
import com.example.ui.theme.ChalkWhite
import com.example.ui.theme.MediumGrey
import com.example.ui.theme.NeonGreen

@Composable
fun BrutalistButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = NeonGreen,
    contentColor: Color = Asphalt,
    icon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .clickable(enabled = enabled, onClick = onClick)
            .background(if (enabled) Color.Black else Color.Black.copy(alpha = 0.3f), shape = RoundedCornerShape(4.dp))
            .offset(x = (-3).dp, y = (-3).dp)
            .border(2.dp, if (enabled) Color.Black else BorderGrey, shape = RoundedCornerShape(4.dp))
            .background(if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.4f), shape = RoundedCornerShape(4.dp))
            .padding(vertical = 14.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text.uppercase(),
                color = contentColor,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
fun BrutalistCard(
    modifier: Modifier = Modifier,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    shadowOffset: Dp = 6.dp,
    shadowColor: Color = Color.Black,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .drawBehind {
                drawRect(
                    color = shadowColor,
                    topLeft = Offset(shadowOffset.toPx(), shadowOffset.toPx()),
                    size = size
                )
            }
            .then(clickableModifier)
            .border(2.dp, borderColor, shape = RoundedCornerShape(4.dp))
            .background(backgroundColor, shape = RoundedCornerShape(4.dp))
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun DashedDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outline,
    thickness: Dp = 2.dp,
    dashLength: Dp = 10.dp,
    gapLength: Dp = 6.dp
) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .drawBehind {
                val pathEffect = PathEffect.dashPathEffect(
                    floatArrayOf(dashLength.toPx(), gapLength.toPx()),
                    0f
                )
                drawLine(
                    color = color,
                    start = Offset(0f, size.height / 2),
                    end = Offset(size.width, size.height / 2),
                    strokeWidth = thickness.toPx(),
                    pathEffect = pathEffect
                )
            }
    )
}

@Composable
fun StencilBadge(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Asphalt,
    textColor: Color = NeonGreen,
    borderColor: Color = NeonGreen
) {
    Box(
        modifier = modifier
            .border(1.dp, borderColor, shape = RoundedCornerShape(2.dp))
            .background(backgroundColor, shape = RoundedCornerShape(2.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text.uppercase(),
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
        )
    }
}
