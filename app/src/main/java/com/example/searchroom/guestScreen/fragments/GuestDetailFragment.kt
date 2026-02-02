package com.example.searchroom.guestScreen.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchroom.R
import com.example.searchroom.databinding.FragmentGuestDetailBinding
import com.example.searchroom.guestScreen.adapter.AmenitiesItemsAdapter
import com.example.searchroom.guestScreen.adapter.ImageSliderAdapter
import com.example.searchroom.guestScreen.model.AmenitiesItems
import com.example.searchroom.guestScreen.model.PgDetails

class GuestDetailFragment : Fragment() {

    private var _binding : FragmentGuestDetailBinding? = null
    private val binding get() = _binding!!

    private val args: GuestDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGuestDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.pgId


        val pg = getPgDetailsById(id)
        Log.d("GuestDetailFragment", "argument Fetch: ${pg.name}")
        binding.detailPgNameTv.text = pg.name
        binding.detailLocationTv.text = pg.location

        // sliding Images
//        setupImageSlider(pg.images)
        setupImageSlider()

        // Amenities Item Lists
        amenitiesItems()

        onBackPress()
    }

    private fun getPgDetailsById(id: String): PgDetails {
        return when (id) {
            "pg1" -> PgDetails(
                name = "Sai Girls PG",
                location = ", Bhilai",
                price = "₹4,500 /mo",
                rating = "4",
                images = listOf(R.drawable.room_image, R.drawable.icon_hostel)
            )
            "pg2" -> PgDetails(
                name = "Palm Residency",
                location = "Nehru Nagar",
                price = "₹6,000 /mo",
                rating = "4.7",
                images = listOf(R.drawable.icon_boys_pg, R.drawable.room_image)
            )
            "pg3" -> PgDetails(
                name = "Palm Residency",
                location = "Nehru Nagar",
                price = "₹6,000 /mo",
                rating = "4.7",
                images = listOf(R.drawable.icon_boys_pg, R.drawable.room_image)
            )
            else -> PgDetails(
                name = "Unknown PG",
                location = "Unknown",
                price = "N/A",
                rating = "0.0",
                images = listOf(R.drawable.room_image)
            )
        }
    }


//    private fun setupImageSlider(images: List<Int>) {
    private fun setupImageSlider() {

//        val adapter = ImageSliderAdapter(images)
//        binding.detailViewPagerImages.adapter = adapter

//        binding.dotsIndicator.attachTo(binding.detailViewPagerImages)

        val imageList = listOf(
            R.drawable.room_image,
            R.drawable.room_image,
            R.drawable.icon_hostel,
            R.drawable.icon_boys_pg
        )
        val adapter = ImageSliderAdapter(imageList)
        binding.detailViewPagerImages.adapter = adapter

        binding.dotsIndicator.attachTo(binding.detailViewPagerImages)
    }

    private fun amenitiesItems() {
        val amenitiesList  = listOf(
            AmenitiesItems(R.drawable.ic_wifi_24dp, "Wifi", "Fast browsing for remote work"),
            AmenitiesItems(R.drawable.ic_wifi_24dp, "Meals", "Fast browsing for remote work"),
            AmenitiesItems(R.drawable.ic_wifi_24dp, "Parking", "Fast browsing for remote work"),
            AmenitiesItems(R.drawable.ic_wifi_24dp, "Security", "Fast browsing for remote work"),
            AmenitiesItems(R.drawable.ic_wifi_24dp, "Wifi", "Fast browsing for remote work"),
            AmenitiesItems(R.drawable.ic_wifi_24dp, "Wifi", "Fast browsing for remote work"),
            AmenitiesItems(R.drawable.ic_wifi_24dp, "Meals", "Fast browsing for remote work"),
            AmenitiesItems(R.drawable.ic_wifi_24dp, "Parking", "Fast browsing for remote work"),
            AmenitiesItems(R.drawable.ic_wifi_24dp, "Security", "Fast browsing for remote work")
        )

        binding.detailRecyclerAmenties.layoutManager = LinearLayoutManager(requireContext())

        binding.detailRecyclerAmenties.adapter = AmenitiesItemsAdapter(amenitiesList)
        binding.detailRecyclerAmenties.setHasFixedSize(true)
    }

    private fun onBackPress() {
        binding.detailBtnback.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}