package com.fccalc.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Світла тема для FC Calc додатку
 */
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = TextPrimary,

    secondary = Accent,
    onSecondary = TextPrimary,
    secondaryContainer = PrimaryLight,
    onSecondaryContainer = TextPrimary,

    tertiary = Success,
    onTertiary = Color.White,

    error = Error,
    onError = Color.White,

    background = Background,
    onBackground = TextPrimary,

    surface = Surface,
    onSurface = TextPrimary,

    surfaceVariant = PrimaryLight,
    onSurfaceVariant = TextSecondary,

    outline = Divider,
    outlineVariant = TextHint
)

/**
 * Тема FC Calc - завжди світла
 */
@Composable
fun FCCalcTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
