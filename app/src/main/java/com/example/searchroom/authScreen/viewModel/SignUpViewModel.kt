package com.example.searchroom.authScreen.viewModel

import android.app.Activity
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchroom.authScreen.helper.GoogleCredentialAuthHelper
import com.example.searchroom.authScreen.repository.AuthRepository
import com.example.searchroom.authScreen.repository.UserRepository
import com.example.searchroom.authScreen.uiState.SignUpUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select

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
                name = name, email = email , password = password, userType = userType
            ) { success, message ->

                if(success) {
                    _uiState.value = SignUpUiState(
                        isLoading = false,
                        navigateTo = userType
                    )
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
            AuthRepository.signInWithGoogle(activity, webClientId){ success, error ->

                if(success) {
                    UserRepository.getUserType { type ->
                        if(type.isNullOrEmpty()) {
                            UserRepository.saveUserType(selectedUserType) { ok ->
                                _uiState.value = SignUpUiState(
                                    isLoading = false,
                                    navigateTo = if(ok) selectedUserType else null,
                                    error = if(ok) null else "Failed to save user type"

                                )
                            }
                        } else {
                            // Existing user -> use stored role
                            _uiState.value = SignUpUiState(
                                isLoading = false,
                                navigateTo = type
                            )
                        }

                    }
                } else {
                    _uiState.value = SignUpUiState(
                        isLoading = false,
                        error = error)
                }

            }
//                onSuccess = {
//                    // After firebase google login success:
//                    UserRepository.getUserType { type ->
//                        if(type.isNullOrEmpty()) {
//                            UserRepository.saveUserType(selectedUserType) { ok ->
//                                if(ok){
//                                    _uiState.value = SignUpUiState(isLoading = false, navigateTo = selectedUserType)
//                                } else {
//                                    _uiState.value = SignUpUiState(isLoading = false, error = "Failed to save user type")
//                                }
//
//                            }
//                        }  else {
//                            _uiState.value = SignUpUiState(isLoading = false, navigateTo = type)
//                        }
//                    }
//                },
//                onError = { msg ->
//                    _uiState.value = SignUpUiState(isLoading = false, error = msg)
//                }
//            )
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