package com.example.searchroom.authScreen.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

// This is for Firestore user profile
object UserRepository {
    private const val TAG = "UserRepository"

    private val auth = FirebaseAuth.getInstance()

    fun saveUserProfile(
        name: String,
        email: String,
        photoUrl: String?,
        userType: String,
        onDone: (Boolean) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: run {
            Log.e(TAG, "User not logged in")
            onDone(false)
            return
        }

        // Create a new user profile
        val userData = hashMapOf(
            "uid" to uid,
            "name" to name,
            "email" to email,
            "photoUrl" to (photoUrl ?: ""),
            "userType" to userType,
            "updatedAt" to com.google.firebase.Timestamp.now()
        )

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .set(userData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "User profile saved")
                onDone(true)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error saving profile", e)
                onDone(false)
            }
    }

    // Fetch userType from Firestore
    fun getUserType(onResult: (String?) -> Unit) {
        val uid = auth.currentUser?.uid ?: run {
            Log.e(TAG, "User not logged in")
            onResult(null)
            return
        }

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                onResult(doc.getString("userType"))
            }
            .addOnFailureListener {
                Log.e(TAG, "Failed to fetch userType", it)
                onResult(null)
            }
    }

}