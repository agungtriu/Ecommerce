package com.agungtriu.ecommerce.core.remote.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ResponsePayment(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("data")
    val data: List<DataTypePayment>? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class DataTypePayment(

    @field:SerializedName("item")
    val item: List<DataPayment?>? = null,

    @field:SerializedName("title")
    val title: String? = null
)

@Parcelize
data class DataPayment(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("label")
    val label: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
) : Parcelable
