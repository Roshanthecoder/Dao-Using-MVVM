package com.example.localdatabaseproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.localdatabaseproject.R
import com.example.localdatabaseproject.databinding.ItemRowProductBinding
import com.example.localdatabaseproject.models.ProductList
import com.example.localdatabaseproject.roomdb.Action

class ProductListAdapter(private val productList: List<ProductList>) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    private var onItemClick: (ProductList, Action) -> Unit = { _, _ -> }
    fun setOnItemClickListener(listener: (ProductList, Action) -> Unit) {
        onItemClick = listener
    }

    inner class ViewHolder(private val binding: ItemRowProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context

        fun bind(data: ProductList) {
            binding.itemNameTextView.text =data.pName
            binding.quantityTextView.text=data.manualQty.toString()
            binding.itemQtyTextView.text = "Total Qty ${data.pQty}"
            Glide.with(context)
                .load(data.pImage)
                .error(R.drawable.ic_launcher_foreground) // Set error image if loading fails
                .into(binding.itemImageView)
            binding.ratePerPiece.text = "Rate Per Piece ${data.pRate}"
            binding.plusButton.setOnClickListener {
                data.manualQty= ++data.manualQty
                binding.quantityTextView.text = data.manualQty.toString()
                // Notify the adapter of data change
                notifyItemChanged(adapterPosition)
            }

            // Minus button click listener
            binding.minusButton.setOnClickListener {
                if (data.manualQty > 0) {
                   data.manualQty= --data.manualQty
                    binding.quantityTextView.text = data.manualQty.toString()
                    // Notify the adapter of data change
                    notifyItemChanged(adapterPosition)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRowProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])
    }
}