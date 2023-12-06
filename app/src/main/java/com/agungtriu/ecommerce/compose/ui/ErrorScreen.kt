package com.agungtriu.ecommerce.compose.ui

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
import java.net.HttpURLConnection

@Composable
fun ErrorScreen(error: ResponseError, hitRefresh: () -> Unit) {
    var title = ""
    var message = ""
    when (error.code) {
        HttpURLConnection.HTTP_NOT_FOUND -> {
            title = stringResource(id = R.string.store_empty)
            message = stringResource(id = R.string.store_empty_desc)
        }

        HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> {
            title = stringResource(id = R.string.store_connection_title)
            message = stringResource(id = R.string.store_connection_desc)
        }

        else -> {
            title = (error.code ?: "").toString()
            message = error.message ?: ""
        }
    }

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
            text = title,
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
            text = message,
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
            onClick = { hitRefresh() },
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