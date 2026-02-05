package com.example.searchroom.guestScreen.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.searchroom.R
import com.example.searchroom.databinding.FragmentGuestHomeBinding
import com.example.searchroom.databinding.ItemPgCardBinding
import com.example.searchroom.databinding.ItemSmallChipsBinding
import com.example.searchroom.guestScreen.GuestActivity
import com.example.searchroom.guestScreen.LocationBottomSheet

class GuestHomeFragment : Fragment() {
    private val TAG = "GuestHomeFragment"

    private var _binding: FragmentGuestHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGuestHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set Category Cards here
        setCategoryCards()

        // Set small chips here
        setChips()

        binding.homeLocationTv.setOnClickListener {
            showLocationBottomSheet()
        }

        binding.homeItemPgCard.pgCardFav.setOnClickListener {

            Log.d(TAG, "Saved in Favorite")
            Toast.makeText(requireContext(), "Saved in Favorite", Toast.LENGTH_SHORT).show()

        }

        // navigate to detail Screen(click on view detail button)
        binding.homeItemPgCard.guestViewDetailsBtn.setOnClickListener {
            navigationDetailScreen("pg1")
        }
    }

    // set Category Card
    private fun setCategoryCards() {

        val boysPgCardBinding = binding.homeBoysPgCard
        val girlsPgCardBinding = binding.homeGirlsPgCard
        val flatCardBinding = binding.homeFlatCard
        val hostelCardBinding = binding.homeHostelCard

        boysPgCardBinding.homeCategoryTvCard.text = getString(R.string.boysPg)
        boysPgCardBinding.homeCategoryImgCard.setImageResource(R.drawable.icon_boys_pg)

        girlsPgCardBinding.homeCategoryTvCard.text = getString(R.string.girlsPg)
        girlsPgCardBinding.homeCategoryImgCard.setImageResource(R.drawable.icon_girl_pg)

        flatCardBinding.homeCategoryTvCard.text = getString(R.string.flat)
        flatCardBinding.homeCategoryImgCard.setImageResource(R.drawable.icon_flat)

        hostelCardBinding.homeCategoryTvCard.text = getString(R.string.hostel)
        hostelCardBinding.homeCategoryImgCard.setImageResource(R.drawable.icon_hostel)

    }

    private fun setChips() {

        val pgCardBinding = binding.homeItemPgCard
        val wifiChip = pgCardBinding.homeChipWifi

        wifiChip.itemChipTv.text = getString(R.string.wifi)
        wifiChip.itemChipImg.setImageResource(R.drawable.ic_wifi_24dp)

        val foodChip = pgCardBinding.homeChipFood

        foodChip.itemChipTv.text = getString(R.string.food)
        foodChip.itemChipImg.setImageResource(R.drawable.ic_food_24dp)

        val acChip = pgCardBinding.homeChipAc

        acChip.itemChipTv.text = getString(R.string.ac)
        acChip.itemChipImg.setImageResource(R.drawable.ic_ac_24dp)
    }

    private fun showLocationBottomSheet() {

        val sheet = LocationBottomSheet(
            onCurrentLocation = { lat, lng ->

                // Update UI (for now)
                binding.homeLocationTv.text = "Near You"

                // later send to viewModel -> listin gdistance filter
            },

            onManualLocation = { city ->
                binding.homeLocationTv.text = city
            }
        )

        sheet.show(parentFragmentManager, "LocationBottomSheet")
    }

    private fun navigationDetailScreen(pg: String) {

        val action = GuestHomeFragmentDirections.actionGuestHomeFragmentToGuestDetailFragment(pg)

        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
