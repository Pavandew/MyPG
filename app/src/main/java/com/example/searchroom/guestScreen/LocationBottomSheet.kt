package com.example.searchroom.guestScreen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.example.searchroom.databinding.BottomsheetLocationBinding
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LocationBottomSheet(
    private val onCurrentLocation: (Double, Double) -> Unit,
    private val onManualLocation: (String) -> Unit,

): BottomSheetDialogFragment() {

    private var _binding: BottomsheetLocationBinding ? = null
    private val binding get() = _binding!!

    private val REQUEST_LOCATION = 10001

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = BottomsheetLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // GPS Current Location
        binding.btnUseCurrentLocation.setOnClickListener {
            getCurrentLocation()
        }

        // Manual select (simple for now: user types city)
        binding.btnSetManualLocation.setOnClickListener {
            val city = binding.etManualLocation.text.toString().trim()

            if(city.isNotEmpty()) {
                onManualLocation(city)
                dismiss()
            }
        }
    }

    // get users current location
    private fun getCurrentLocation() {
        val context = requireContext()

        if(ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
            return
        }

        val fused = LocationServices.getFusedLocationProviderClient(context)

        fused.lastLocation.addOnSuccessListener { loc ->
            if(loc != null) {
                Log.d("LocationBottomSheet", "Current Location: ${loc.latitude}, ${loc.longitude}")
                onCurrentLocation(loc.latitude, loc.longitude)
                dismiss()

            }
        }

    }

    // request permission
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode != REQUEST_LOCATION && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}