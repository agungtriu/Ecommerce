package com.agungtriu.ecommerce.core.utils

import com.google.gson.annotations.SerializedName

data class ResponseNotification(

    @field:SerializedName("message")
    val message: Message? = null
)

data class Data(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("time")
    val time: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("body")
    val body: String? = null,

    @field:SerializedName("type")
    val type: String? = null
)

data class Message(

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("topic")
    val topic: String? = null
)
