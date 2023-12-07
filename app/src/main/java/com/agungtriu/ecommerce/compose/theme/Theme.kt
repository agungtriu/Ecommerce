package com.agungtriu.ecommerce.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            surface = Color.Black,
        )
    } else {
        lightColorScheme(
            surface = Color.White
        )
    }

    MaterialTheme(
        colorScheme = colors,
        content = content,
    )
}
