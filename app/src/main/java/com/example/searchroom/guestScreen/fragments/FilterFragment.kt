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
import com.example.searchroom.R
import com.example.searchroom.databinding.FragmentFilterBinding
import com.example.searchroom.guestScreen.model.PgFilter
import com.example.searchroom.guestScreen.model.PriceSort
import com.example.searchroom.guestScreen.viewModel.ListingViewModel
import kotlinx.coroutines.launch

class FilterFragment : Fragment() {

    private val TAG = "FilterFragment"

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private val listViewModel: ListingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpClickListeners()

        // when user come back to filter fragment
        observeExistingFilter()
    }

    private fun setUpClickListeners() {

        binding.filterApplyFilters.setOnClickListener {
            Toast.makeText(requireContext(), "Filtered list", Toast.LENGTH_SHORT).show()

            val filter = PgFilter(
                priceSort = when(binding.rgPriceSort.checkedRadioButtonId) {
                    R.id.rbLowToHigh -> PriceSort.LOW_TO_HIGH
                    R.id.rbHighToLow -> PriceSort.HIGH_TO_LOW
                    else -> null
                },

                roomType = when(binding.cgRoomType.checkedChipId) {
                    R.id.chipSingle -> "Single"
                    R.id.chipDouble -> "Double"
                    R.id.chipTriple -> "Triple"
                    R.id.chipDorm -> "Dorm"
                    else -> null
                },

                gender = when( binding.cgGender.checkedChipId) {
                    R.id.chipBoys -> "Boys"
                    R.id.chipGirls -> "Girls"
                    R.id.chipUnisex -> "Unisex"
                    else -> null

                },

                distanceKm = when (binding.cgDistance.checkedChipId) {
                    R.id.chip1km -> 1
                    R.id.chip3km -> 3
                    R.id.chip5km -> 5
                    R.id.chip10km -> 10
                    else -> null
                },

                hasAC = when( binding.cgAC.checkedChipId) {
                    R.id.chipAnyAc -> true
                    R.id.chipAc -> true
                    else -> null
                }
            )

            Log.d(TAG, "Filter Cleared $filter")

            listViewModel.setFilter(filter)
            findNavController().navigateUp()
        }


        binding.filterBtnClear.setOnClickListener {
            binding.rgPriceSort.clearCheck()
            binding.cgGender.clearCheck()
            binding.cgRoomType.clearCheck()
            binding.cgAC.clearCheck()
            binding.cgDistance.clearCheck()

            listViewModel.clearFilter()
        }

        binding.filterApplyFilters.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeExistingFilter() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                listViewModel.filter.collect { f ->

                    when (f.priceSort) {
                        PriceSort.LOW_TO_HIGH -> binding.rgPriceSort.check(R.id.rbLowToHigh)
                        PriceSort.HIGH_TO_LOW -> binding.rgPriceSort.check(R.id.rbHighToLow)
                        else -> binding.rgPriceSort.clearCheck()
                    }

                    when(f.gender) {
                        "boys" -> binding.cgGender.check(R.id.chipBoys)
                        "girls" -> binding.cgGender.check(R.id.chipGirls)
                        "unisex" -> binding.cgGender.check(R.id.chipUnisex)
                        else -> binding.cgGender.clearCheck()
                    }

                    when (f.hasAC) {
                        true -> binding.cgAC.check(R.id.chipAc)
                        false -> binding.cgAC.check(R.id.chipNonAc)
                        null -> binding.cgAC.clearCheck()
                    }

                    when (f.distanceKm){
                        1 -> binding.cgDistance.check(R.id.chip1km)
                        3 -> binding.cgDistance.check(R.id.chip3km)
                        5 -> binding.cgDistance.check(R.id.chip5km)
                        10 -> binding.cgDistance.check(R.id.chip10km)
                        else -> binding.cgDistance.clearCheck()
                    }

                    Log.d(TAG, "Filter Cleared")
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}