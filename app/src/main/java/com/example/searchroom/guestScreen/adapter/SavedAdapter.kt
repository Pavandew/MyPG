package com.example.searchroom.guestScreen.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.searchroom.databinding.ItemSavedListBinding
import com.example.searchroom.guestScreen.model.PgListing

class SavedAdapter(
    private var savedItems: List<PgListing> = emptyList(),
    private val onDetailItemClick: (item: PgListing) -> Unit,
): RecyclerView.Adapter<SavedAdapter.SavedViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<PgListing>) {
        savedItems = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavedAdapter.SavedViewHolder {
        val view = ItemSavedListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SavedViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedAdapter.SavedViewHolder, position: Int) {

        val items = savedItems[position]

        holder.name.text = items.pgName
//        holder.location.text = items.location
        holder.price.text = "₹${items.price} / month"
        holder.image.load(items.images.firstOrNull())

        holder.itemView.setOnClickListener {
            onDetailItemClick(items)
        }
    }
    override fun getItemCount(): Int {
        return savedItems.size
    }

    inner class SavedViewHolder(
        private val itemsavedbinding: ItemSavedListBinding
    ): RecyclerView.ViewHolder(itemsavedbinding.root){

        val name = itemsavedbinding.savedTitleTv
        val location = itemsavedbinding.savedLocationTv
        val price = itemsavedbinding.savedPriceTv
        val image = itemsavedbinding.savedImg
        val btnFav = itemsavedbinding.savedBtnFav


    }
}