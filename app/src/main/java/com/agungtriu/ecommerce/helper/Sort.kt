package com.agungtriu.ecommerce.helper

enum class Sort(val id: String, val en: String) {
    REVIEW("Ulasan", "Review"),
    SOLD("Penjualan", "Sold"),
    MAX_PRICE("Harga Tertinggi", "Highest"),
    MIN_PRICE("Harga Terendah", "Lowest")
}