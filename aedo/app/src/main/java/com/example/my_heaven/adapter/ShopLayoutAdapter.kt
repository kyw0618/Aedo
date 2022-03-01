package com.example.my_heaven.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.my_heaven.R
import com.example.my_heaven.databinding.ItemActivityShopBinding

data class Sample(
    val title: String,
    val oPrice: String,
    val sPrice: String,
    val rate: String
)

class ShopLayoutAdapter : RecyclerView.Adapter<ShopLayoutAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Sample>() {

        override fun areItemsTheSame(oldItem: Sample, newItem: Sample): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Sample, newItem: Sample): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemActivityShopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(newList: List<Sample>) = differ.submitList(newList)

    class ViewHolder(private val binding: ItemActivityShopBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Sample) {
            binding.apply {
                ivActivityShopItem.setImageResource(R.drawable.activity_shop_sample_image)
                tvActivityShopTitle.text = "3단 기본형 근조화환"
                tvActivityShopOriginalPrice.text = "88,000원"
                tvActivityShopSalesPrice.text = "77,000"
                tvActivityShopSalesRates.text = "4%"

                tvActivityShopOriginalPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
    }
}