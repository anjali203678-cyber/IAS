package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val LightLavenderColorScheme = lightColorScheme(
    primary = DeepPurple,
    onPrimary = PureWhite,
    primaryContainer = SoftLilac,
    onPrimaryContainer = DeepPurple,
    secondary = LavenderPurple,
    onSecondary = DeepPurple,
    secondaryContainer = BabyPink,
    onSecondaryContainer = DeepPurple,
    tertiary = MemberJiminColor,
    onTertiary = PureWhite,
    tertiaryContainer = BabyPink,
    onTertiaryContainer = DeepPurple,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,
    outline = LightOutline,
    outlineVariant = SilverMist,
    error = ErrorRose,
    onError = PureWhite,
    errorContainer = ErrorRoseContainer,
    onErrorContainer = ErrorRose
)

private val DarkGalaxyColorScheme = darkColorScheme(
    primary = LavenderPurple,
    onPrimary = DeepPurple,
    primaryContainer = DarkGalaxySurfaceElevated,
    onPrimaryContainer = SoftLilac,
    secondary = BabyPink,
    onSecondary = DeepPurple,
    secondaryContainer = ColorTokens.DarkPinkContainer,
    onSecondaryContainer = BabyPink,
    tertiary = MemberJhopeColor,
    onTertiary = DeepPurple,
    background = DarkGalaxyBg,
    onBackground = DarkTextPrimary,
    surface = DarkGalaxySurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkGalaxySurfaceElevated,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkGalaxyOutline,
    outlineVariant = DarkGalaxyOutline,
    error = ColorTokens.DarkError,
    onError = DeepPurple,
    errorContainer = ColorTokens.DarkErrorContainer,
    onErrorContainer = BabyPink
)

private object ColorTokens {
    val DarkPinkContainer = androidx.compose.ui.graphics.Color(0xFF47254E)
    val DarkError = androidx.compose.ui.graphics.Color(0xFFFF9DB5)
    val DarkErrorContainer = androidx.compose.ui.graphics.Color(0xFF5A1E32)
}

val PurpleDreamShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(30.dp)
)

@Composable
fun CivilsPurpleDreamTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkGalaxyColorScheme else LightLavenderColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = PurpleDreamShapes,
        content = content
    )
}
