package com.example.ui.theme

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DreamyGalaxyBackground(
    isDarkGalaxy: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "galaxy_twinkle")
    val twinkle by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "twinkle_alpha"
    )

    val bgBrush = if (isDarkGalaxy) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF120B24),
                Color(0xFF1F133D),
                Color(0xFF160E2C)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFF9F5FF),
                Color(0xFFF3EBFE),
                Color(0xFFFDF4FA)
            )
        )
    }

    Box(modifier = modifier.fillMaxSize().background(bgBrush)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val starColor = if (isDarkGalaxy) SoftLilac else LavenderPurple
            val pinkMist = if (isDarkGalaxy) BabyPink.copy(alpha = 0.08f) else BabyPink.copy(alpha = 0.35f)
            val lilacMist = if (isDarkGalaxy) LavenderPurple.copy(alpha = 0.08f) else SoftLilac.copy(alpha = 0.65f)

            // Soft dreamy nebula clouds
            drawCircle(
                color = lilacMist,
                radius = size.width * 0.42f,
                center = Offset(size.width * 0.15f, size.height * 0.12f)
            )
            drawCircle(
                color = pinkMist,
                radius = size.width * 0.38f,
                center = Offset(size.width * 0.88f, size.height * 0.25f)
            )
            drawCircle(
                color = lilacMist,
                radius = size.width * 0.45f,
                center = Offset(size.width * 0.75f, size.height * 0.85f)
            )

            // Subtle twinkling stars & four-point sparkles
            val stars = listOf(
                Triple(0.12f, 0.09f, 5f),
                Triple(0.34f, 0.06f, 3.5f),
                Triple(0.78f, 0.08f, 6f),
                Triple(0.90f, 0.18f, 4f),
                Triple(0.08f, 0.42f, 4.5f),
                Triple(0.92f, 0.56f, 5.5f),
                Triple(0.22f, 0.78f, 4f),
                Triple(0.84f, 0.90f, 5f)
            )
            stars.forEachIndexed { index, (fx, fy, r) ->
                val phase = if (index % 2 == 0) twinkle else 1.35f - twinkle
                val alpha = (phase.coerceIn(0.2f, 0.9f)) * if (isDarkGalaxy) 0.75f else 0.45f
                drawSparkleStar(
                    center = Offset(size.width * fx, size.height * fy),
                    radius = r.dp.toPx(),
                    color = starColor.copy(alpha = alpha)
                )
            }
        }
        content()
    }
}

private fun DrawScope.drawSparkleStar(center: Offset, radius: Float, color: Color) {
    val path = Path().apply {
        val inner = radius * 0.28f
        for (i in 0 until 8) {
            val angle = Math.toRadians((i * 45.0) - 90.0)
            val dist = if (i % 2 == 0) radius else inner
            val x = center.x + (dist * cos(angle)).toFloat()
            val y = center.y + (dist * sin(angle)).toFloat()
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }
    drawPath(path = path, color = color)
}

@Composable
fun PurpleDreamCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f),
    cornerRadius: Dp = 22.dp,
    tonalElevation: Dp = 2.dp,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = tonalElevation)
    ) {
        content()
    }
}
