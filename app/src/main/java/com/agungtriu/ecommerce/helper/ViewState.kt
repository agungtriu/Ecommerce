package com.agungtriu.ecommerce.helper

import com.agungtriu.ecommerce.core.remote.model.response.ResponseError

sealed class ViewState<out T> {
    object Loading : ViewState<Nothing>()
    data class Success<T>(val data: T) : ViewState<T>()
    data class Error(val error: ResponseError) : ViewState<Nothing>()
}