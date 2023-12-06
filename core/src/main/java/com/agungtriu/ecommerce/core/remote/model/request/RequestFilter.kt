package com.agungtriu.ecommerce.core.remote.model.request

data class RequestFilter(
    val search: String? = null,
    val brand: String? = null,
    val lowest: Int? = null,
    val highest: Int? = null,
    val sort: String? = null,
    val limit: Int? = null,
    val page: Int? = null
) {
    fun toMap(): MutableMap<String, Any?> {
        return mutableMapOf(
            "search" to search,
            "brand" to brand,
            "lowest" to lowest,
            "highest" to highest,
            "sort" to sort,
            "limit" to limit,
            "page" to page
        )
    }
}
