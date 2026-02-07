package com.example.searchroom.hostScreen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.searchroom.databinding.FragmentHostListingBinding

class HostListingFragment : Fragment() {

    private var _binding : FragmentHostListingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHostListingBinding.inflate(inflater, container, false)

        return binding.root
    }
}