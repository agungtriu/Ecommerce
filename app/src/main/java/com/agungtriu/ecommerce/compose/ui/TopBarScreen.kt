package com.agungtriu.ecommerce.compose.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavController
import com.agungtriu.ecommerce.R
import com.google.firebase.analytics.FirebaseAnalytics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarScreen(findNavController: NavController, title: String, analytics: FirebaseAnalytics) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        title = {
            Text(
                text = title,
                fontFamily = FontFamily(Font(R.font.poppins_400)),
                style = MaterialTheme.typography.titleLarge.plus(
                    TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false,
                        )
                    )
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                analytics.logEvent("btn_back", null)
                findNavController.navigateUp()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = ""
                )
            }
        }
    )
}
