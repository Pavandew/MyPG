package com.example.searchroom.guestScreen.model


data class PgListing(
    val pgId: String,
    val pgName: String,
    val price: String,
    val location: String,
    val rating: Double,
    val images: List<Int>
)
