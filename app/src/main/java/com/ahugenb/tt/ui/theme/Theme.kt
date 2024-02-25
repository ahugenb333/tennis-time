package com.ahugenb.tt.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AustralianOpenTheme = lightColorScheme(
    primary = AOWhite,
    onPrimary = Color.Black,
    secondary = AOBlue,
    onSecondary = AOWhite,
    background = AOBlue,
    onBackground = AOWhite,
    surface = AOWhite,
    onSurface = AOBlue
    // Add other colors if necessary
)

private val FrenchOpenTheme = darkColorScheme(
    primary = FOGreen,
    onPrimary = Color.White,
    secondary = FOOrange,
    onSecondary = Color.White,
    background = FOOrange,
    onBackground = FOGreen,
    surface = FOGreen,
    onSurface = FOOrange,
    // Add other colors if necessary
)


@Composable
fun TennisTimeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // todo use slam calendar for this
    // Dynamic color is available on Android 12+
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> FrenchOpenTheme
        else -> AustralianOpenTheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.secondary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TennisTypography,
        content = content
    )
}