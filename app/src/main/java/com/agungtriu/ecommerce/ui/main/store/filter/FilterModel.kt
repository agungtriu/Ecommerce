package com.agungtriu.ecommerce.ui.main.store.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterModel(
    var sort: String? = null,
    var category: String? = null,
    var min: Int? = null,
    var max: Int? = null
) : Parcelable
