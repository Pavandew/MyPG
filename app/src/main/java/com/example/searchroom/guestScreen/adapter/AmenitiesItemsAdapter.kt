package com.example.searchroom.guestScreen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.searchroom.R
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.searchroom.guestScreen.model.AmenitiesItems

class AmenitiesItemsAdapter(
    val amenitiesItems: List<AmenitiesItems>
) : RecyclerView.Adapter<AmenitiesItemsAdapter.AmentiesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AmenitiesItemsAdapter.AmentiesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_amenities, parent, false,)

        return AmentiesViewHolder(view)
    }

    override fun onBindViewHolder(holder: AmenitiesItemsAdapter.AmentiesViewHolder, position: Int) {
        val items = amenitiesItems[position]

        holder.title.text = items.title
        holder.subTitle.text = items.subTitle

        holder.icon.load(items.icon)
    }

    override fun getItemCount(): Int {
        return amenitiesItems.size
    }

    inner class AmentiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val icon = itemView.findViewById<ImageView>(R.id.amenties_icon)
        val title = itemView.findViewById<TextView>(R.id.amenties_title)
        val subTitle = itemView.findViewById<TextView>(R.id.amenties_sub_title)

    }
}