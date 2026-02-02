package com.example.searchroom.guestScreen

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.searchroom.R
import com.example.searchroom.databinding.ActivityGuestBinding

class GuestActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGuestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGuestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.guest_container) as NavHostFragment

        val navController = navHostFragment.navController

        binding.guestBottomNav.setupWithNavController(navController)

        val bottomNavScreens = setOf(
            R.id.guestHomeFragment,
            R.id.guestSearchFragment,
            R.id.guestListingFragment,
            R.id.guestProfileFragment
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.guestBottomNav.isVisible = destination.id in bottomNavScreens
        }

    }
}