package com.example.searchroom.authScreen.repository

import android.app.Activity
import android.util.Log
import com.example.searchroom.authScreen.helper.GoogleCredentialAuthHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

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
        name: String,
        email: String,
        password: String,
        userType: String,
        onResult: (Boolean, String?) -> Unit
    ){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid

                if(uid == null) {
                    onResult(false, "UID not found")
                    return@addOnSuccessListener
                }

                val data = hashMapOf(
                    "name" to name,
                    "email" to email,
                    "userType" to userType,
                    "createAt" to FieldValue.serverTimestamp()
                )

                database().collection("users")
                    .document(uid)
                    .set(data, SetOptions.merge())
                    .addOnSuccessListener {
                        Log.d(TAG, "Profile saved for userName: $name")
                        onResult(true, null)
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error saving profile", e)
                        onResult(false, e.message)
                    }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error signing up", e)
                onResult(false, e.message)
            }
    }
}