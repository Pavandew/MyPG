package com.example.searchroom.hostScreen.repository

import com.example.searchroom.hostScreen.model.UserProfileData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// Firestore Read and Update
class UserRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private fun userDoc() =
        database.collection("users")
            .document(auth.currentUser?.uid ?: "")

    suspend fun getMyProfile(): UserProfileData{

        val uid = auth.currentUser?.uid ?: throw IllegalArgumentException("Not logged in")
        val doc = database.collection("users")
            .document(uid).get().await()

        val updateAt = doc.getTimestamp("updatedAt")?.toDate()?.time ?: 0L

        return UserProfileData(
            uid = doc.getString("uid").orEmpty(),
            name = doc.getString("name").orEmpty(),
            email = doc.getString("email").orEmpty(),
            photoUrl = doc.getString("photoUrl").orEmpty(),
            userType = doc.getString("userType").orEmpty(),
            updatedAt =  updateAt
        )
    }

    suspend fun updateProfile(
        name: String,
        photoUrl: String
    ) {
        val uid = auth.currentUser?.uid ?: throw IllegalArgumentException("NOt logged in")

        database.collection("users").document(uid)
            .update(
                mapOf(
                    "name" to name,
                    "photoUrl" to photoUrl,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
    }


}