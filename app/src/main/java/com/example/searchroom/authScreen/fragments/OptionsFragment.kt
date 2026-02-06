package com.example.searchroom.authScreen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.searchroom.R
import com.google.android.material.card.MaterialCardView
import androidx.core.content.edit

class OptionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_options, container, false)


        val hostCard = view.findViewById<MaterialCardView>(R.id.card_Host)
        val guestCard = view.findViewById<MaterialCardView>(R.id.card_Guest)


        hostCard.setOnClickListener {
            saveUserRole("HOST")
            val action = OptionsFragmentDirections.actionOptionsFragmentToLoginFragment("HOST")
            Toast.makeText(requireContext(), "Host Clicked", Toast.LENGTH_SHORT).show()
            findNavController().navigate(action)
        }

        guestCard.setOnClickListener {
            saveUserRole("GUEST")
            val action = OptionsFragmentDirections.actionOptionsFragmentToLoginFragment("GUEST")
            Toast.makeText(requireContext(), "Guest Clicked", Toast.LENGTH_SHORT).show()
            findNavController().navigate(action)
        }

        return view
    }

    private fun saveUserRole(role: String) {
        requireContext()
            .getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
            .edit {
                putBoolean("hasSeenOption", true)
                putString("selected_role", role)

            }
    }

}