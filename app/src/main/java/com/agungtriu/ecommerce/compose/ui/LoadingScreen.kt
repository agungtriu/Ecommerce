package com.agungtriu.ecommerce.compose.ui

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun LoadingScreen() {
    CircularProgressIndicator(
        color = MaterialTheme.colorScheme.primary
    )
}
