package com.example.searchroom.authScreen.helper

import android.app.Activity
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleCredentialAuthHelper(
    private val activity: Activity,
    private val webClientId: String

) {
    private val TAG = "GoogleAuth"
    private val auth = FirebaseAuth.getInstance()
    private val credentialManager = CredentialManager.create(activity)

    suspend fun signIn(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d(TAG, "Starting Goggle Credential Sign-in flow")

        try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(webClientId)
                .setFilterByAuthorizedAccounts(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = activity
            )

            Log.d(TAG, "Received credential: $result")
            val credential = result.credential

            if(credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

                val googleIdTokenCredential = try {
                    GoogleIdTokenCredential.createFrom(credential.data)
                } catch (e: GoogleIdTokenParsingException) {
                    Log.e(TAG, "Error parsing Google ID token", e)
                    onError("Google token parse failed")
                    return
                }

                val idToken = googleIdTokenCredential.idToken
                Log.d(TAG, "Received ID token")

                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            Log.d(TAG, "Firebase Goggle sign-in successful. UID: ${auth.currentUser?.uid}")
                            onSuccess()
                        } else {
                            Log.e(TAG, "Firebase Goggle sign-in failed", task.exception)
                            onError(task.exception?.message ?: "Firebase auth failed")

                        }
                    }
            } else {
                Log.e(TAG, "Received credential is not of type Google ID token")
                onError("Invalid credential type")

            }
        } catch (e: GetCredentialException) {
            Log.e(TAG, "Error getting credential", e)
            onError(e.message ?: "Credential retrieval failed")
        }
    }
}