package com.example.searchroom.guestScreen.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchroom.R
import com.example.searchroom.databinding.FragmentGuestListingBinding
import com.example.searchroom.guestScreen.GuestActivity
import com.example.searchroom.guestScreen.adapter.ListingAdapter
import com.example.searchroom.guestScreen.model.PgListing
import com.example.searchroom.guestScreen.viewModel.ListingViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class GuestListingFragment : Fragment() {
    private val TAG = "ListingFragment"

    private var _binding: FragmentGuestListingBinding? = null
    private val binding get() = _binding!!

    private val listviewModel: ListingViewModel by activityViewModels()
    private lateinit var listingAdapter: ListingAdapter

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
        if (tabLayout.tabCount == 0) {
            tabLayout.addTab(tabLayout.newTab().setText("Recommended"))
            tabLayout.addTab(tabLayout.newTab().setText("Near to you"))
        }

        listingAdapter = ListingAdapter(
            onDetailItemClick = { pg -> onViewDetailClicked(pg)},
            onFavItemClick = { onSavedClicked()}

        )
        binding.listingRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.listingRecyclerview.adapter = listingAdapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> listviewModel.setTab(0)
                    1 -> listviewModel.setTab(1)
//                    0 -> {
//                        loadRecommended()
//                    }
//                    1 -> loadNearToYou()

                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }
        })

        observer()
        listviewModel.loadDummyData(
            recommended = loadRecommended(),
            near = loadNearToYou()
        )
        listviewModel.setTab(0)
    }

    private fun observer() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                listviewModel.displayList.collect { list ->

                    Log.d(TAG, "Showing list size = ${list.size}")
                    listingAdapter.submitList(list)
                }
            }
        }

    }

    private fun onFilterBtnClicked() {
        val action = GuestListingFragmentDirections.actionGuestListingFragmentToFilterFragment()
        findNavController().navigate(action)
    }

    private fun onViewDetailClicked(pg: PgListing) {

        Log.d(TAG, "Detail Clicked PG -> id: ${pg.pgId}, name: ${pg.pgName}")

        val action = GuestListingFragmentDirections
            .actionGuestListingFragmentToGuestDetailFragment(pg.pgId)

        findNavController().navigate(action)

    }

    private fun onSavedClicked() {

        Log.d(TAG, "BookMark Clicked ")
        Toast.makeText(requireContext(), "BookMark Clicked", Toast.LENGTH_SHORT).show()

        // navigate to saved fragment and back stack works correct for bottom navigation tab
//        (requireContext() as GuestActivity).openSavedTab()

//        val action = GuestListingFragmentDirections
//            .actionGuestListingFragmentToGuestSavedFragment()
//
//        findNavController().navigate(action)

    }

    private fun loadRecommended() : List<PgListing> {

        return listOf(
            PgListing(
                pgId = "pg1",
                pgName = "Sai Girl PG",
                price = 10000,
                "male",
                hasAC = true,
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
                price = 10000,
                "male",
                hasAC = true,
                rating = 3.5,
                images = listOf(
                    R.drawable.room_image,
                    R.drawable.icon_boys_pg,
                    R.drawable.app_logo,
                )
            )
        )
//        Log.d(TAG, "Recommended list size = ${list.size}")
//
//        listingAdapter.updateList(newItems = list)
    }

    private fun loadNearToYou(): List<PgListing> {
        return listOf(
            PgListing(
                pgId = "pg3",
                pgName = "City Stay PG",
                price = 10000,
                gender ="female",
                hasAC = true,
                rating = 3.5,
                images = listOf(
                    R.drawable.room_image,
                    R.drawable.app_logo,
                    )
            ),
            PgListing(
                pgId = "pg4",
                pgName = "City Boys PG",
                price = 10000,
                "male",
                hasAC = false,
                rating = 3.5,
                images = listOf(
                    R.drawable.app_logo,
                )
            )
        )
//        Log.d(TAG, "Near to you list size = ${list.size}")
//        listingAdapter.updateList(newItems = list)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}