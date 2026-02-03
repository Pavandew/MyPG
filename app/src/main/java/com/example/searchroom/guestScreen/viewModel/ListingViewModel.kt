package com.example.searchroom.guestScreen.viewModel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.searchroom.guestScreen.model.PgFilter
import com.example.searchroom.guestScreen.model.PgListing
import com.example.searchroom.guestScreen.model.PriceSort
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ListingViewModel: ViewModel() {

    private val TAG = "PG_VM"

    private val _filter = MutableStateFlow(PgFilter())
    val filter: StateFlow<PgFilter> = _filter

    private val _allRecommended = MutableStateFlow<List<PgListing>>(emptyList())

    private val _allNear = MutableStateFlow<List<PgListing>>(emptyList())

    // result list after filter
    private val _displayList = MutableStateFlow<List<PgListing>>(emptyList())
    val displayList : StateFlow<List<PgListing>> = _displayList

    private var selectedTab = 0 // 0 for recommended, 1 for near to me


    private var userLat: Double? = null
    private var userLng: Double? = null


    fun setTab(position: Int) {
        selectedTab = position
        applyFilters()
    }

    fun setFilter(newFilter: PgFilter) {
        Log.d(TAG, "New filter applied: $newFilter" )
        _filter.value = newFilter
        applyFilters()
    }

    fun clearFilter() {
        Log.d(TAG, "Filters cleared")
        _filter.value = PgFilter()
        applyFilters()
    }

    fun loadDummyData(recommended: List<PgListing>, near: List<PgListing>) {
        if (_allRecommended.value.isNotEmpty() || _allNear.value.isNotEmpty()) {
            Log.d(TAG, "Dummy data already loaded, skipping")
            applyFilters()
            return

            Log.d(TAG, "Loading dummy PG list")
        }

        _allRecommended.value = recommended
        _allNear.value = near
        applyFilters()
    }

    fun setUserLocation(lat: Double, lng: Double) {
        Log.d(TAG, "User location set: $lat, $lng")
        userLat = lat
        userLng = lng
        applyFilters()
    }

    private fun applyFilters() {
        val base = if (selectedTab == 0) {
            _allRecommended.value
        } else {
            _allNear.value
        }

        var out = base
        val f = _filter.value
        Log.d(TAG, "Applying filters: $f")

        // room type
//        f.roomType?.let { rt -> out = out.filter { it.roomType == rt  } }

        // gender
        f.gender?.let { g -> out = out.filter { it.gender == g } }

        // ac
        f.hasAC?.let { ac -> out = out.filter { it.hasAC == ac } }

        // price sort
        out = when(f.priceSort) {
            PriceSort.LOW_TO_HIGH -> out.sortedBy { it.price }
            PriceSort.HIGH_TO_LOW -> out.sortedByDescending { it.price }
            else -> out
        }


//        val lat = userLat
//        val lng = userLng
//        if (f.distanceKm != null && lat != null && lng != null) {
//            val maxKm = f.distanceKm.toDouble()
//            out = out.filter { pg ->
//                distanceInKm(lat, lng, pg.lat, pg.lng) <= maxKm
//            }
//            Log.d(TAG, "Distance filter applied: ${f.distanceKm}km → ${out.size} left")
//        }

        _displayList.value = out
    }

    private fun distanceInKm(aLat: Double, aLng: Double, bLat: Double, bLng: Double): Double {
        val res = FloatArray(1)
        Location.distanceBetween(aLat, aLng, bLat, bLng, res)
        return res[0] / 1000.0
    }

}