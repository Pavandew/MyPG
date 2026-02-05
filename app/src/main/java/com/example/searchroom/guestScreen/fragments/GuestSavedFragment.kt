package com.example.searchroom.guestScreen.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchroom.databinding.FragmentGuestSavedBinding
import com.example.searchroom.guestScreen.adapter.SavedAdapter
import com.example.searchroom.guestScreen.model.PgListing

class GuestSavedFragment : Fragment() {
    private var _binding: FragmentGuestSavedBinding? = null
    private val binding get() = _binding!!

    private lateinit var savedAdapter: SavedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGuestSavedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedAdapter = SavedAdapter(
            onDetailItemClick = { item -> onDetailItemClick(item)}
        )

        binding.savedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.savedRecyclerView.adapter = savedAdapter

    }

    private fun onDetailItemClick(item: PgListing) {
        

        val action = GuestSavedFragmentDirections.actionGuestSavedFragmentToGuestDetailFragment(item.pgId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}