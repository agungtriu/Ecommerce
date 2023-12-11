package com.agungtriu.ecommerce.helper

enum class Sort(val id: String, val en: String) {
    RATING("Ulasan", "Rating"),
    SALE("Penjualan", "Sale"),
    HIGHEST("Harga Tertinggi", "Highest"),
    LOWEST("Harga Terendah", "Lowest")
}
