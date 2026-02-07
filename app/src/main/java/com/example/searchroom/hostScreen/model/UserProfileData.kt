package com.example.searchroom.hostScreen.model

data class UserProfileData(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val userType: String = "",
    val updatedAt: Long = 0L
)
