package com.example.searchroom.guestScreen.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchroom.R
import com.example.searchroom.databinding.FragmentGuestListingBinding
import com.example.searchroom.guestScreen.adapter.ListingAdapter
import com.example.searchroom.guestScreen.model.PgListing
import com.google.android.material.tabs.TabLayout

class GuestListingFragment : Fragment() {
    private val TAG = "ListingFragment"

    private var _binding: FragmentGuestListingBinding? = null
    private val binding get() = _binding!!

    private lateinit var listingAdapter: ListingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGuestListingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listingFilterBtn.setOnClickListener {
            onFilterBtnClicked()
        }

        val tabLayout = binding.listingTabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Recommended"))
        tabLayout.addTab(tabLayout.newTab().setText("Near to you"))

        listingAdapter = ListingAdapter(
            onItemClick = { pg -> onViewDetailClicked(pg)}
        )
        binding.listingRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.listingRecyclerview.adapter = listingAdapter

        loadRecommended()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> {

                        loadRecommended()
                    }
                    1 -> loadNearToYou()

                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }
        })
    }

    private fun onFilterBtnClicked() {
        val action = GuestListingFragmentDirections.actionGuestListingFragmentToFilterFragment()
        findNavController().navigate(action)
    }

    private fun onViewDetailClicked(pg: PgListing) {

        Log.d(TAG, "Clicked PG -> id: ${pg.pgId}, name: ${pg.pgName}")

        val action = GuestListingFragmentDirections
            .actionGuestListingFragmentToGuestDetailFragment(pg.pgId)

        findNavController().navigate(action)

    }

    private fun loadRecommended() {

        val list = listOf(
            PgListing(
                pgId = "pg1",
                pgName = "Sai Girl PG",
                price = "10000",
                location = "Bhilai, Durg",
                rating = 4.5,
                images = listOf(
                    R.drawable.room_image,
                    R.drawable.icon_flat,
                    R.drawable.icon_girl_pg,
                    R.drawable.room_image,
                )

            ),
            PgListing(
                pgId = "pg2",
                pgName = "Sai Boys PG",
                price = "10000",
                location = "Raipur",
                rating = 4.5,
                images = listOf(
                    R.drawable.room_image,
                    R.drawable.icon_boys_pg,
                    R.drawable.app_logo,
                )
            )
        )
        Log.d(TAG, "Recommended list size = ${list.size}")

        listingAdapter.updateList(newItems = list)
    }

    private fun loadNearToYou() {
        val list = listOf(
            PgListing(
                pgId = "pg3",
                pgName = "City Stay PG",
                price = "10000",
                location = "Bemetara",
                rating = 4.5,
                images = listOf(
                    R.drawable.room_image,
                    R.drawable.app_logo,
                    )
            ),
            PgListing(
                pgId = "pg4",
                pgName = "City Boys PG",
                price = "9000",
                location = "Bilaspur",
                rating = 4.5,
                images = listOf(
                    R.drawable.app_logo,
                )
            )
        )
        Log.d(TAG, "Near to you list size = ${list.size}")

        listingAdapter.updateList(newItems = list)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}