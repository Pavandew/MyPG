package com.example.searchroom.guestScreen.repository

import android.util.Log
import com.example.searchroom.R
import com.example.searchroom.guestScreen.model.PgListing

class PgFilterRepository {

    fun getDummyPgs(): List<PgListing> {
        Log.d("PG_REPO", "Loading dummy PG Data")

        return listOf(
            PgListing("1", "Sai PG", 6000, "male",
                true,  21.1938, 81.3509,
                4.2, listOf(R.drawable.room_image))
        )
    }
}