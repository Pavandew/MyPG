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

class SignUpViewModel: ViewModel() {
    private val TAG = "SignUpViewModel"

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState

    // Email and Password Sign Up
    fun signUpWithEmailPassword(
        name: String,
        email: String,
        password: String,
        rePassword: String,
        userType: String
    ) {
        if(!isValid(name, email, password, rePassword)) return

        _uiState.value = SignUpUiState(isLoading = true)

        viewModelScope.launch {
            AuthRepository.signUpWithEmailPassword(
                email = email , password = password
            ) { success, message ->

                if(success) {
                    UserRepository.saveUserProfile(
                        name = name,
                        email = email,
                        photoUrl = null,
                        userType = userType
                    ) { ok ->
                        _uiState.value = SignUpUiState(
                            isLoading = false,
                            navigateTo = if(ok) userType else null,
                            error = if(ok) null else "Failed to Save profile"
                        )
                    }

                    Log.d(TAG, "Signup success")
                } else {
                    _uiState.value = SignUpUiState(
                        isLoading = false,
                        error = message ?: "Signup failed"
                    )
                    Log.d(TAG, "Signup failed: $message")
                }
            }
        }
    }

    // Google Sign In
    fun googleSignIn(
        activity: Activity,
        webClientId: String,
        selectedUserType: String
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

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearNavigation() {
        _uiState.value = _uiState.value.copy(navigateTo = null)
    }

    private fun isValid(
        name: String,
        email: String,
        password: String,
        rePassword: String
    ) : Boolean {

        if(name.isBlank()) {
            _uiState.value = SignUpUiState(error = "Enter name")
            return false
        }

        if(email.isBlank()) {
            _uiState.value = SignUpUiState(error = "Enter email")
            return false
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = SignUpUiState(error = "Enter a valid email Address")
            return false
        }

        if(password.length < 6) {
            _uiState.value = SignUpUiState(error = "Password must be at least 6 characters")
            return false
        }

        if(rePassword != password) {
            _uiState.value = SignUpUiState(error = "Passwords do not match")
            return false
        }
        return true
    }
}