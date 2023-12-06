package com.agungtriu.ecommerce.ui.review.compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import com.agungtriu.ecommerce.core.remote.model.response.DataReview

@Composable
fun ReviewsContent(reviews: List<DataReview>) {
    LazyColumn {
        items(reviews.size, key = { it }) {
            ReviewItem(item = reviews[it])
        }
    }
}
