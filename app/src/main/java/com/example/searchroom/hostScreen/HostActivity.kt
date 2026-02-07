package com.example.searchroom.hostScreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.searchroom.R
import com.example.searchroom.databinding.ActivityHostBinding

class HostActivity : AppCompatActivity() {
    lateinit var binding: ActivityHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.host_container) as NavHostFragment

        val navController = navHostFragment.navController

        // connect bottom navigation
        binding.hostBottomNav.setupWithNavController(navController)


    }
}