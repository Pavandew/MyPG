package com.example.searchroom.hostScreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.searchroom.databinding.ItemBookingRequestBinding
import com.example.searchroom.hostScreen.model.BookingModel

class BookingRequestAdapter(
    private val onAcceptedClick: (BookingModel) -> Unit,
    private val onRejectedClick: (BookingModel) -> Unit
): RecyclerView.Adapter<BookingRequestAdapter.BookingViewHolder>() {

    private var items: List<BookingModel> = emptyList()

    fun submitList(list: List<BookingModel>) {
        items = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookingRequestAdapter.BookingViewHolder {
        val view = ItemBookingRequestBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BookingRequestAdapter.BookingViewHolder,
        position: Int
    ) {
        val item = items[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class BookingViewHolder(
        private val binding: ItemBookingRequestBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BookingModel) {
            val name = binding.hostHomeReqNameTv
            val date = binding.hostHomeReqDateTv
            val image = binding.hostHomeReqImg

            name.text = item.name
            date.text = item.date.toString()
            image.load(item.image)

            binding.hostHomeRequestAcceptBtn.setOnClickListener {
                onAcceptedClick(item)
            }

            binding.hostHomeRequestRejectBtn.setOnClickListener {
                onRejectedClick(item)
            }

        }
    }
}