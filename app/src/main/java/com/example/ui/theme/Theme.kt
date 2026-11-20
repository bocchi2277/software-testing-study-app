package com.example.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Immutable
data class QaSemanticColors(
    val ok: Color,
    val okBg: Color,
    val bad: Color,
    val badBg: Color,
    val warn: Color,
    val warnBg: Color,
    val muted: Color,
    val border: Color,
    val sidebar: Color
)

private val LightSemantic = QaSemanticColors(
    ok = LightOk,
    okBg = LightOkBg,
    bad = LightBad,
    badBg = LightBadBg,
    warn = LightWarn,
    warnBg = LightWarnBg,
    muted = LightMuted,
    border = LightBorder,
    sidebar = LightSidebar
)

private val DarkSemantic = QaSemanticColors(
    ok = DarkOk,
    okBg = DarkOkBg,
    bad = DarkBad,
    badBg = DarkBadBg,
    warn = DarkWarn,
    warnBg = DarkWarnBg,
    muted = DarkMuted,
    border = DarkBorder,
    sidebar = DarkSidebar
)

val LocalQaColors = staticCompositionLocalOf { LightSemantic }
val LocalQaIsDarkTheme = staticCompositionLocalOf { false }

private val LightColorScheme = lightColorScheme(
    primary = LightAccent,
    onPrimary = LightOnAccent,
    primaryContainer = Color(0xFFCDE9E7),
    onPrimaryContainer = LightSidebar,
    secondary = LightSidebar,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDCE8E7),
    onSecondaryContainer = LightText,
    background = LightBg,
    onBackground = LightText,
    surface = LightSurface,
    onSurface = LightText,
    surfaceVariant = Color(0xFFE4ECEB),
    onSurfaceVariant = LightMuted,
    outline = LightBorder,
    error = LightBad,
    onError = Color.White,
    errorContainer = LightBadBg,
    onErrorContainer = LightBad
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkAccent,
    onPrimary = DarkOnAccent,
    primaryContainer = Color(0xFF114644),
    onPrimaryContainer = Color(0xFFCDE9E7),
    secondary = DarkAccent,
    onSecondary = DarkOnAccent,
    secondaryContainer = Color(0xFF1E3234),
    onSecondaryContainer = DarkText,
    background = DarkBg,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText,
    surfaceVariant = Color(0xFF1D2F31),
    onSurfaceVariant = DarkMuted,
    outline = DarkBorder,
    error = DarkBad,
    onError = DarkOnAccent,
    errorContainer = DarkBadBg,
    onErrorContainer = DarkBad
)

private val AppShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp)
)

fun resolveIsDarkTheme(themeMode: String, systemDark: Boolean): Boolean = when (themeMode) {
    "dark" -> true
    "light" -> false
    else -> systemDark
}

@Composable
private fun animThemeColor(target: Color, label: String): Color {
    val color by animateColorAsState(
        targetValue = target,
        animationSpec = tween(durationMillis = 240),
        label = label
    )
    return color
}

@Composable
private fun animatedColorScheme(target: ColorScheme): ColorScheme = target.copy(
    primary = animThemeColor(target.primary, "primary"),
    onPrimary = animThemeColor(target.onPrimary, "onPrimary"),
    primaryContainer = animThemeColor(target.primaryContainer, "primaryContainer"),
    onPrimaryContainer = animThemeColor(target.onPrimaryContainer, "onPrimaryContainer"),
    secondary = animThemeColor(target.secondary, "secondary"),
    onSecondary = animThemeColor(target.onSecondary, "onSecondary"),
    secondaryContainer = animThemeColor(target.secondaryContainer, "secondaryContainer"),
    onSecondaryContainer = animThemeColor(target.onSecondaryContainer, "onSecondaryContainer"),
    background = animThemeColor(target.background, "background"),
    onBackground = animThemeColor(target.onBackground, "onBackground"),
    surface = animThemeColor(target.surface, "surface"),
    onSurface = animThemeColor(target.onSurface, "onSurface"),
    surfaceVariant = animThemeColor(target.surfaceVariant, "surfaceVariant"),
    onSurfaceVariant = animThemeColor(target.onSurfaceVariant, "onSurfaceVariant"),
    outline = animThemeColor(target.outline, "outline"),
    error = animThemeColor(target.error, "error"),
    onError = animThemeColor(target.onError, "onError"),
    errorContainer = animThemeColor(target.errorContainer, "errorContainer"),
    onErrorContainer = animThemeColor(target.onErrorContainer, "onErrorContainer")
)

@Composable
private fun animatedSemanticColors(target: QaSemanticColors): QaSemanticColors = QaSemanticColors(
    ok = animThemeColor(target.ok, "ok"),
    okBg = animThemeColor(target.okBg, "okBg"),
    bad = animThemeColor(target.bad, "bad"),
    badBg = animThemeColor(target.badBg, "badBg"),
    warn = animThemeColor(target.warn, "warn"),
    warnBg = animThemeColor(target.warnBg, "warnBg"),
    muted = animThemeColor(target.muted, "muted"),
    border = animThemeColor(target.border, "border"),
    sidebar = animThemeColor(target.sidebar, "sidebar")
)

@Composable
fun QaPathTheme(
    themeMode: String = "auto",
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val darkTheme = resolveIsDarkTheme(themeMode, systemDark)
    val colorScheme = animatedColorScheme(if (darkTheme) DarkColorScheme else LightColorScheme)
    val semantic = animatedSemanticColors(if (darkTheme) DarkSemantic else LightSemantic)

    CompositionLocalProvider(
        LocalQaColors provides semantic,
        LocalQaIsDarkTheme provides darkTheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShapes,
            content = content
        )
    }
}

