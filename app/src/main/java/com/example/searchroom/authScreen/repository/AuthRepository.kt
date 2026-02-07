package com.example.searchroom.authScreen.repository

import android.app.Activity
import com.example.searchroom.authScreen.helper.GoogleCredentialAuthHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// this repository for authentication
object AuthRepository {

    private const val TAG = "AuthRepository"

    private val auth = FirebaseAuth.getInstance()
    private fun database() = FirebaseFirestore.getInstance()

    suspend fun signInWithGoogle(
        activity: Activity,
        webClientId: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val helper = GoogleCredentialAuthHelper(activity, webClientId)

        helper.signIn(
            onSuccess = { onResult(true, null) },
            onError = { msg -> onResult(false, msg) }
        )
    }

    fun loginWithEmailPassword(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    // Sign up with email and password
    fun signUpWithEmailPassword(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }

    }

    fun logOut() {
        FirebaseAuth.getInstance().signOut()
    }
}