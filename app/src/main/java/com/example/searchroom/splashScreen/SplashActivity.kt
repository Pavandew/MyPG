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

        val splash = installSplashScreen()

        super.onCreate(savedInstanceState)

        var keep = true
        splash.setKeepOnScreenCondition { keep }

        // Move to next Screen
        window.decorView.postDelayed({
            keep = false

            val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
            val role = prefs.getString("selected_role", null)
            val isLoggedIn = FirebaseAuth.getInstance().currentUser != null

            val nextIntent = if(isLoggedIn && role != null) {

                when(role) {
                    "HOST" -> Intent(this, HostActivity::class.java)
                    "GUEST" -> Intent(this, GuestActivity::class.java)
                    else -> Intent(this, AuthActivity::class.java)
                }
            } else {
                Intent(this, AuthActivity::class.java)
            }

            startActivity(nextIntent)
            finish()
        }, 1200)



        // For Custom Splash Screen
//        setContentView(R.layout.activity_splash)

//        val tvTitle = findViewById<TextView>(R.id.splash_title)
//        val imageLogo = findViewById<ImageView>(R.id.splash_imageView)
//
//
//        // Animations for Image
//        imageLogo.animate()
//            .alpha(1f)
//            .scaleX(1.5f)
//            .scaleY(1.5f)
//            .setDuration(1000)
//            .setStartDelay(150)
//            .start()
//
//        // Animation for Text
//        tvTitle.animate()
//            .alpha(1f)
//            .scaleX(1.5f)
//            .scaleY(1.5f)
//            .setDuration(1000)
//            .setStartDelay(250)
//            .start()

//        Handler(Looper.getMainLooper()).postDelayed({
//            startActivity(Intent(this, AuthActivity::class.java))
//            finish()
//        }, 2000)

    }

}