package com.example.searchroom.authScreen.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

object UserRepository {
    private const val TAG = "UserRepository"

    private val auth = FirebaseAuth.getInstance()

    // Save userType to Firestore
    fun saveUserType(userType: String, onDone: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid ?: run {
            Log.e(TAG, "User not logged in")
            onDone(false)
            return
        }

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .set(mapOf("userType" to userType),
                SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "UserType saved: $userType")
                onDone(true)
            }
            .addOnFailureListener {e ->
                Log.e(TAG, "Error saving userType", e)
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