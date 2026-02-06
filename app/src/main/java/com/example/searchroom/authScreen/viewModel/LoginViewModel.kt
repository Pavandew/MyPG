package com.example.searchroom.authScreen.viewModel

import android.app.Activity
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchroom.authScreen.repository.AuthRepository
import com.example.searchroom.authScreen.repository.UserRepository
import com.example.searchroom.authScreen.uiState.SignUpUiState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private companion object {
        const val TAG = "LoginViewModel"
    }

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState

    fun loginWithMail(email: String, password: String) {

        if(!isValid(email, password)) {
            return
        }

        _uiState.value = SignUpUiState(isLoading = true)

        viewModelScope.launch {
            AuthRepository.loginWithEmailPassword(email, password) { success, message ->
                if(success) {
                    // fetch userType from FireStore and navigate
                    UserRepository.getUserType { type ->
                        val finalType = type ?: "GUEST"
                        Log.d(TAG, "Firestore userType: $finalType")

                        _uiState.value = SignUpUiState(isLoading = false, navigateTo = finalType)
                    }
                } else {
                    Log.d(TAG, "Login error with message: $message")
                    _uiState.value = SignUpUiState(isLoading = false, error = message ?: "Login failed")
                }
            }
        }
    }

    fun loginWithGoogle(
        activity: Activity,
        webClientId: String,
        selectedUserType: String,
    ) {
        _uiState.value = SignUpUiState(isLoading = true)

        viewModelScope.launch {
            AuthRepository.signInWithGoogle(activity, webClientId) { success, error ->

                if (success) {
                    val user = FirebaseAuth.getInstance().currentUser
                    Log.d(TAG, "Google Sign In success: $user")

                    UserRepository.getUserType { existingType ->
                        val finalRole = existingType ?: selectedUserType
                        Log.d(TAG, "Firestore userType: $finalRole")

                        UserRepository.saveUserProfile(
                            name = user?.displayName ?: "",
                            email = user?.email ?: "",
                            photoUrl = user?.photoUrl?.toString(),
                            userType = finalRole
                        ) { ok ->
                            _uiState.value = SignUpUiState(
                                isLoading = false,
                                navigateTo = if (ok) finalRole else null,
                                error = if (ok) null else "Failed to save profile"
                            )
                        }
                    }
                } else {
                    _uiState.value = SignUpUiState(
                        isLoading = false,
                        error = error
                    )
                }
            }
        }
    }

    fun clearLoginError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearLoginNavigation() {
        _uiState.value = _uiState.value.copy(navigateTo = null)
    }
    private fun isValid(email: String, password: String): Boolean {

        if(email.isBlank()) {
            _uiState.value = SignUpUiState(error = "Enter email")
            return false
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = SignUpUiState(error = "Enter a valid email Address")
            return false
        }

        if (password.isBlank()) {
            _uiState.value = SignUpUiState(error = "Enter password")
            return false
        }
        return true
    }
}