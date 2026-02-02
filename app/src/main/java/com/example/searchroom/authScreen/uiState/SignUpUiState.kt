package com.example.searchroom.authScreen.uiState

data class SignUpUiState(
    val isLoading : Boolean =  false,
    val error: String? = null,
    val navigateTo: String?= null
)