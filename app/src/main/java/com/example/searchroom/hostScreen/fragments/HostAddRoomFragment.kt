package com.example.searchroom.hostScreen.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.searchroom.R
import com.example.searchroom.databinding.FragmentHostAddRoomBinding
import com.example.searchroom.databinding.FragmentHostHomeBinding

class HostAddRoomFragment : Fragment() {

    private var _binding : FragmentHostAddRoomBinding ? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHostAddRoomBinding.inflate(inflater, container, false)

        return binding.root
    }
}