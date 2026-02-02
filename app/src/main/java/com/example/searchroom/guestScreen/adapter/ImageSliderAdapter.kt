package com.example.searchroom.guestScreen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.searchroom.R

class ImageSliderAdapter(
    private val imageList: List<Int>
) : RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageSliderAdapter.ImageViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_slider, parent, false)

        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageSliderAdapter.ImageViewHolder, position: Int) {
        val currentImage = imageList[position]

        holder.imageView.load(currentImage) {
            placeholder(R.drawable.room_image)
                .error(R.drawable.room_image)
                .crossfade(true)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageSliderItem)

    }
}