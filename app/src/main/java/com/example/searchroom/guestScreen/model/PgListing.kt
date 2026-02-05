package com.example.searchroom.guestScreen.model


data class PgListing(
    val pgId: String,
    val pgName: String,
    val price: Int = 0,
    val gender: String = "",
    val hasAC: Boolean = false,
    val lat: Double = 0.0,
    val lng: Double = 0.0,
//    val locationName:String = "",
    val rating: Double,
    val images: List<Int> = emptyList()
)
