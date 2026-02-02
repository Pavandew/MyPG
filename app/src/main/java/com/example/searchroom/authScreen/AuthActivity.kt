package com.example.searchroom.authScreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.searchroom.R

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_activty)

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val hasSeenOption = prefs.getBoolean("hasSeenOption", false)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.auth_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        val graph = navController.navInflater.inflate(R.navigation.auth_nav_graph)
        graph.setStartDestination(if (hasSeenOption) R.id.loginFragment else R.id.optionsFragment)
        navController.graph = graph
    }
}