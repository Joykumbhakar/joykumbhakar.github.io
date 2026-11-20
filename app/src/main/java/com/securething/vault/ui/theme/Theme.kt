package com.securething.vault.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val OneUIDarkColorScheme = darkColorScheme(
    primary = OneUIBlueLight,
    onPrimary = Color.White,
    primaryContainer = OneUIBlueContainerDark,
    onPrimaryContainer = OneUIBlueLight,

    secondary = OneUIPurple,
    onSecondary = Color.White,
    secondaryContainer = OneUIPurple.copy(alpha = 0.2f),
    onSecondaryContainer = OneUIPurple,

    background = OneUIDarkCanvas,
    onBackground = OneUIDarkTextPrimary,

    surface = OneUIDarkSurface,
    onSurface = OneUIDarkTextPrimary,

    surfaceVariant = OneUIDarkSurfaceVariant,
    onSurfaceVariant = OneUIDarkTextSecondary,

    outline = OneUIDarkDivider,

    error = OneUIError,
    onError = Color.White
)

val OneUILightColorScheme = lightColorScheme(
    primary = OneUIBlue,
    onPrimary = Color.White,
    primaryContainer = OneUIBlueContainerLight,
    onPrimaryContainer = OneUIBlue,

    secondary = OneUIPurple,
    onSecondary = Color.White,
    secondaryContainer = OneUIPurple.copy(alpha = 0.15f),
    onSecondaryContainer = OneUIPurple,

    background = OneUILightCanvas,
    onBackground = OneUILightTextPrimary,

    surface = OneUILightSurface,
    onSurface = OneUILightTextPrimary,

    surfaceVariant = OneUILightSurfaceVariant,
    onSurfaceVariant = OneUILightTextSecondary,

    outline = OneUILightDivider,

    error = OneUIError,
    onError = Color.White
)

@Composable
fun VaultTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> OneUIDarkColorScheme
        else -> OneUILightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
