package com.agungtriu.ecommerce.compose.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.agungtriu.ecommerce.R
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    stars: Int = 5,
    starsColor: Color = Color.Yellow,
    size: Int = 20
) {
    val filledStars = floor(rating).toInt()
    val unfilledStars = (stars - ceil(rating)).toInt()
    Row(modifier = modifier) {
        repeat(filledStars) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = null,
                tint = starsColor,
                modifier = Modifier.size(size.dp)
            )
        }
        repeat(unfilledStars) {
            Icon(
                painter = painterResource(id = R.drawable.ic_star_outline),
                contentDescription = null,
                tint = starsColor,
                modifier = Modifier.size(size.dp)
            )
        }
    }
}
