package com.example.searchroom.guestScreen.model

data class PgFilter(
    val priceSort: PriceSort? = null,
    val roomType: String? = null,
    val gender: String? = null,
    val distanceKm: Int? = null,
    val hasAC: Boolean? = null
)

enum class PriceSort {
    LOW_TO_HIGH,
    HIGH_TO_LOW
}

