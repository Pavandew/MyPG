package com.example.searchroom.guestScreen.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.searchroom.R
import com.example.searchroom.authScreen.AuthActivity
import com.example.searchroom.authScreen.repository.AuthRepository
import com.example.searchroom.databinding.FragmentGuestSettingBinding

class GuestSettingFragment : Fragment() {

    private var _binding: FragmentGuestSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGuestSettingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpClicks()
        setUpRows()
    }

    private fun setUpClicks() {
        binding.guestCardEditProfile.setOnClickListener {
            // Navigate to edit profile
        }

        binding.guestSettingSwitchUser.root.setOnClickListener {
            // Switch logic
        }

        binding.guestSettingSavedPg.root.setOnClickListener {
            // Navigate to Saved PG screen
        }

        binding.settingCardSupport.setOnClickListener {
            // Open Help screen
        }

        binding.settingCardLogout.setOnClickListener {
            // Show logout confirmation
            logOutDialog()
        }
    }

    private fun setUpRows() {

        // Edit Profile
        binding.guestSettingEditProfile.ivIcon.setImageResource(R.drawable.ic_person_edit_24dp)
        binding.guestSettingEditProfile.tvTitle.text = "Edit Profile"
        binding.guestSettingEditProfile.badge.visibility = View.GONE

        // Switch User
        binding.guestSettingSwitchUser.ivIcon.setImageResource(R.drawable.ic_switch_user_24dp)
        binding.guestSettingSwitchUser.tvTitle.text = "Switch User"
        binding.guestSettingSwitchUser.badge.visibility = View.GONE

        // saved PGs
        binding.guestSettingSavedPg.ivIcon.setImageResource(R.drawable.ic_bookmark_24dp)
        binding.guestSettingSavedPg.tvTitle.text = "Saved PGs"
        binding.guestSettingSavedPg.badge.visibility = View.VISIBLE
        binding.guestSettingSavedPg.badge.text = "5"

        // 🔹 Help & Support
        binding.guestSettingHelp.ivIcon.setImageResource(R.drawable.ic_help_center_24dp)
        binding.guestSettingHelp.tvTitle.text = "Help & Support"
        binding.guestSettingHelp.badge.visibility = View.GONE

        // 🔹 Logout (Red)
        binding.guestSettingLogout.ivIcon.setImageResource(R.drawable.ic_logout_24dp)
        binding.guestSettingLogout.tvTitle.text = "Logout"
        binding.guestSettingLogout.tvTitle.setTextColor(
            requireContext().getColor(android.R.color.holo_red_light)
        )
        binding.guestSettingLogout.badge.visibility = View.GONE
    }

    private fun logOutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("LogOut")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                // Perform logout
                Log.d("GuestSettingFragment", "Logging out")
                AuthRepository.logOut()

                val prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                prefs.edit().remove("selected_role").apply()

                val intent = Intent(requireContext(), AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
