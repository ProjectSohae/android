package com.sohae.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    /*
    primary = Onyx,
    onPrimary = AshGray,
    secondary = DimGray,
    onSecondary = AshGray,
    tertiary = CadetGray,
    onTertiary = AshGray,
    background = Black,
    onBackground = AshGray,
    surface = Onyx,
    onSurface = AshGray,
     */
    primary = UltraViolet,
    onPrimary = Isabelline,
    primaryContainer = SpaceCadet,
    onPrimaryContainer = UltraViolet,
    secondary = RoseQuartz,
    onSecondary = Isabelline,
    tertiary = PaleDogwood,
    onTertiary = Isabelline,
    background = Color.Transparent,
    onBackground = Isabelline,
    surface = UltraViolet,
    onSurface = Isabelline,
)

private val LightColorScheme = lightColorScheme(
    primary = Navy,
    onPrimary = White,
    primaryContainer = Navy,
    onPrimaryContainer = White,
    secondary = LightNavy,
    onSecondary = White,
    tertiary = Grey,
    onTertiary = White,
    background = Color.Transparent,
    onBackground = White,
    surface = Navy,
    onSurface = Navy,
)

@Composable
fun GongikTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}