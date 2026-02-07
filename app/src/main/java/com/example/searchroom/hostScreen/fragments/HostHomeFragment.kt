package com.example.searchroom.hostScreen.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.searchroom.R
import com.example.searchroom.databinding.FragmentHostHomeBinding
import com.example.searchroom.hostScreen.HostActivity
import com.example.searchroom.hostScreen.adapter.BookingRequestAdapter
import com.example.searchroom.hostScreen.model.BookingModel
import com.example.searchroom.hostScreen.uiState.ProfileUiState
import com.example.searchroom.hostScreen.viewModel.ProfileViewModel
import kotlinx.coroutines.launch

class HostHomeFragment : Fragment() {

    private var _binding : FragmentHostHomeBinding ? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()

    private lateinit var bookingAdapter: BookingRequestAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHostHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpClicks()

        setUpRecyclerView()

        profileViewModel.loadProfile()
        observeProfile()
    }

    private fun observeProfile() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                profileViewModel.profileUiState.collect { state ->

                    when(state) {
                        is ProfileUiState.isLoading -> {  }

                        is ProfileUiState.Success -> {

                            val profile = state.profile

                            binding.hostWelcomeNameTv.text = "Welcome 👋 ${profile.name}"
                            binding.hostHomeProfileImage.load(profile.photoUrl) {
                                placeholder(R.drawable.icon_person_24dp)
                                    .error(R.drawable.icon_person_24dp)
                                    .crossfade(true)
                            }
                        }
                        is ProfileUiState.Error -> {
                            Toast.makeText(
                                requireContext(),
                                state.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
            }

        }
    }

    private fun setUpRecyclerView() {

        // display requesting list
        recyclerView = binding.hostHomeRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        bookingAdapter = BookingRequestAdapter(
            onAcceptedClick = {
                onAcceptedClick(it)
            },
            onRejectedClick = {
                onRejectedClick(it)
            })

        dummyList()
        recyclerView.adapter = bookingAdapter
    }
    // Set up bottom navigation
    private fun setUpClicks() {
        binding.hostHomeCards.cardAddPg.setOnClickListener {
            Toast.makeText(requireContext(), "Add Room Clicked", Toast.LENGTH_SHORT).show()

            (requireActivity() as HostActivity).binding.hostBottomNav.selectedItemId = R.id.hostAddRoomFragment
        }

        binding.hostHomeCards.cardManageMyPg.setOnClickListener {

            Toast.makeText(requireContext(), "Manage Room Clicked", Toast.LENGTH_SHORT).show()
            (requireContext() as HostActivity).binding.hostBottomNav.selectedItemId = R.id.hostListingFragment
        }

        binding.hostHomeCards.cardBookingRequest.setOnClickListener {

            Toast.makeText(requireContext(), "Booking Clicked", Toast.LENGTH_SHORT).show()
            (requireContext() as HostActivity).binding.hostBottomNav.selectedItemId = R.id.hostBookingFragment
        }
    }

    private fun dummyList() {
        val items = listOf(
            BookingModel(
                name = "Pavan Dewangan",
                date = "15 Oct",
                image = R.drawable.room_image),
            BookingModel(
                name = "Pavan Dewangan",
                date = "15 Oct",
                image = R.drawable.room_image),
            BookingModel(
                name = "Pavan Dewangan",
                date = "15 Oct",
                image = R.drawable.room_image),
            BookingModel(
                name = "Pavan Dewangan",
                date = "15 Oct",
                image = R.drawable.room_image),
        )
        bookingAdapter.submitList(items)
    }
    private fun onAcceptedClick(item: BookingModel) {
        Toast.makeText(requireContext(), "Accepted Clicked", Toast.LENGTH_SHORT).show()

    }
    private fun onRejectedClick(item: BookingModel) {
        Toast.makeText(requireContext(), "Rejected Clicked", Toast.LENGTH_SHORT).show()

    }
}