package com.example.searchroom.splashScreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.searchroom.authScreen.AuthActivity
import com.example.searchroom.guestScreen.GuestActivity
import com.example.searchroom.hostScreen.HostActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        // Modern Android Splash API
        installSplashScreen()

        super.onCreate(savedInstanceState)

        navigateUser()
    }

    private fun navigateUser() {

        val currentUser = FirebaseAuth.getInstance().currentUser
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val role = prefs.getString("selected_role", null)

        val nextIntent = when {
            currentUser == null -> {
                // Not logged in
                Intent(this, AuthActivity::class.java)
            }

            role == "HOST" -> {
                Intent(this, HostActivity::class.java)
            }

            role == "GUEST" -> {
                Intent(this, GuestActivity::class.java)
            }

            else -> {
                // If role missing or corrupted
                clearSession()
                Intent(this, AuthActivity::class.java)
            }
        }

        startActivity(nextIntent)
        finish()
    }

    private fun clearSession() {
        FirebaseAuth.getInstance().signOut()
        getSharedPreferences("app_prefs", MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}
