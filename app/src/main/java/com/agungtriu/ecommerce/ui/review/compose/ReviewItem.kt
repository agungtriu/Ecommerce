package com.agungtriu.ecommerce.ui.review.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.compose.ui.RatingBar
import com.agungtriu.ecommerce.core.remote.model.response.DataReview

@Composable
fun ReviewItem(item: DataReview) {
    Column {
        Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)) {
            AsyncImage(
                model = item.userImage,
                contentDescription = null,
                modifier = Modifier.weight(1F).clip(CircleShape)
            )
            Column(
                Modifier
                    .weight(9F)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = item.userName ?: "",
                    fontFamily = FontFamily(Font(R.font.poppins_600)),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
                RatingBar(
                    rating = (item.userRating ?: 0).toDouble(),
                    starsColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    size = 15
                )
            }
        }
        Text(
            text = item.userReview ?: "", style = MaterialTheme.typography.bodySmall,
            fontFamily = FontFamily(Font(R.font.poppins_400)),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Divider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier
                .padding(top = 16.dp)
        )
    }
}