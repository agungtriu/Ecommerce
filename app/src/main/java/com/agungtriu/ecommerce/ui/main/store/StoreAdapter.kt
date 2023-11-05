package com.agungtriu.ecommerce.ui.main.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.remote.model.response.Product
import com.agungtriu.ecommerce.databinding.ItemStoreGridBinding
import com.agungtriu.ecommerce.databinding.ItemStoreLinearBinding
import com.agungtriu.ecommerce.helper.Extension.toRupiah
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class StoreAdapter :
    PagingDataAdapter<Product, RecyclerView.ViewHolder>(callback) {

    private var viewType = 1
    fun setItemViewType(viewType: Int) {
        this.viewType = viewType
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    private inner class ViewLinearHolder(private val binding: ItemStoreLinearBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            Glide.with(itemView.context).load(item.image)
                .transform(CenterInside(), RoundedCorners(8)).placeholder(R.mipmap.ic_thumbnail)
                .into(binding.ivItemStoreLinear)

            binding.tvItemStoreLinearTitle.text = item.productName
            binding.tvItemStoreLinearPrice.text = item.productPrice?.toRupiah()
            binding.tvItemStoreLinearStore.text = item.store
            binding.tvItemStoreLinearRating.text = item.productRating.toString()
            binding.tvItemStoreLinearSold.text =
                itemView.context.getString(R.string.item_sold_title).plus(" ${item.sale}")
        }
    }

    private inner class ViewGridHolder(private val binding: ItemStoreGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            Glide.with(itemView.context).load(item.image).placeholder(R.mipmap.ic_thumbnail)
                .into(binding.ivItemStoreGrid)

            binding.tvItemStoreGridTitle.text = item.productName
            binding.tvItemStoreGridPrice.text = item.productPrice?.toRupiah()
            binding.tvItemStoreGridStore.text = item.store
            binding.tvItemStoreGridRating.text = item.productRating.toString()
            binding.tvItemStoreGridSold.text =
                itemView.context.getString(R.string.item_sold_title).plus(" ${item.sale}")
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ONE) {
            return ViewLinearHolder(
                ItemStoreLinearBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return ViewGridHolder(
                ItemStoreGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            if (getItemViewType(position) == VIEW_TYPE_ONE) {
                (holder as ViewLinearHolder).bind(item)
            } else {
                (holder as ViewGridHolder).bind(item)
            }
        }
    }

    companion object {
        val callback = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Product, newItem: Product) =
                oldItem.productId == newItem.productId
        }
        const val VIEW_TYPE_ONE = 1
    }
}