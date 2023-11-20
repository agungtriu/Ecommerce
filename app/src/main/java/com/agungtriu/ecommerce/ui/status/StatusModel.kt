package com.agungtriu.ecommerce.ui.status

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatusModel(
    val date: String? = null,
    val total: Int? = null,
    val invoiceId: String? = null,
    val payment: String? = null,
    val time: String? = null,
    val status: Boolean? = null,
    val rating: Int? = null,
    val review: String? = null
) : Parcelable
