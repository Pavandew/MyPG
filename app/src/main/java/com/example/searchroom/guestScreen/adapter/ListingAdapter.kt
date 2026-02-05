package com.example.searchroom.guestScreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.searchroom.R
import com.example.searchroom.databinding.ItemPgCardBinding
import com.example.searchroom.guestScreen.model.PgListing

class ListingAdapter(
    private var items: List<PgListing> = emptyList(),
    private val onDetailItemClick: (PgListing) -> Unit,
    private val onFavItemClick: (PgListing) -> Unit
): RecyclerView.Adapter<ListingAdapter.ListingViewHolder>() {

    fun submitList(newItems: List<PgListing>) {
        items = newItems
    }

    companion object DiffCallback : DiffUtil.ItemCallback<PgListing>() {
        override fun areItemsTheSame(oldItem: PgListing, newItem: PgListing): Boolean {

            return oldItem.pgId == newItem.pgId

        }

        override fun areContentsTheSame(oldItem: PgListing, newItem: PgListing): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListingAdapter.ListingViewHolder {
        val binding = ItemPgCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)

        return ListingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListingAdapter.ListingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ListingViewHolder(private val binding: ItemPgCardBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PgListing) {
            binding.pgCardPgName.text = item.pgName
//            binding.pgCardTvOnlyFor.text =item.price
//            binding.homeTvPrice.text = item.price
//            binding.pgCardLocation.text = item.location
            binding.pgCardRating.text = item.rating.toString()

            binding.pgCardImage.load(item.images.firstOrNull()) {
                placeholder(R.drawable.room_image)
                    .error(R.drawable.room_image)
                    .crossfade(true)
            }

            binding.guestViewDetailsBtn.setOnClickListener {
                onDetailItemClick(item)
            }

            binding.pgCardFav.setOnClickListener {
                onFavItemClick(item)
            }
        }
    }
}
