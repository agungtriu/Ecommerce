package com.agungtriu.ecommerce.compose.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.remote.model.response.ResponseError
import com.google.firebase.analytics.FirebaseAnalytics
import java.net.HttpURLConnection

@Composable
fun ErrorScreen(
    responseError: ResponseError,
    context: Context,
    analytics: FirebaseAnalytics,
    hitRefresh: () -> Unit
) {
    val error = errorHandling(responseError, context)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1F))
        Image(
            painter = painterResource(id = R.mipmap.ic_error),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp),
            alignment = Alignment.Center
        )
        Text(
            text = error.title,
            fontSize = 32.sp,
            fontFamily = FontFamily(Font(R.font.poppins_500)),
            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false,
                )
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
        )
        Text(
            text = error.message,
            fontFamily = FontFamily(Font(R.font.poppins_400)),
            style = MaterialTheme.typography.bodyLarge.plus(
                TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false,
                    )
                )
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        )
        Button(
            onClick = {
                analytics.logEvent("btn_error_refresh", null)
                hitRefresh()
            },
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        ) {
            Text(
                text = stringResource(id = R.string.all_refresh),
                fontFamily = FontFamily(Font(R.font.poppins_500)),
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false,
                    )
                )
            )
        }
        Spacer(modifier = Modifier.weight(1F))
    }
}

fun errorHandling(error: ResponseError, context: Context): ErrorMessage {
    return when (error.code) {
        HttpURLConnection.HTTP_NOT_FOUND ->
            ErrorMessage(
                title = context.getString(R.string.store_empty),
                message = context.getString(R.string.store_empty_desc)
            )

        HttpURLConnection.HTTP_GATEWAY_TIMEOUT ->
            ErrorMessage(
                title = context.getString(R.string.store_connection_title),
                message = context.getString(R.string.store_connection_desc)
            )

        else ->
            ErrorMessage(
                title = (error.code ?: "").toString(),
                message = error.message ?: ""
            )
    }
}
