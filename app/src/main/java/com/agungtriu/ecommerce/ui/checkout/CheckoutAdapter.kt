package com.agungtriu.ecommerce.ui.checkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.databinding.ItemCheckoutBinding
import com.agungtriu.ecommerce.helper.Extension.toRupiah
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar

class CheckoutAdapter(private val viewModel: CheckoutViewModel) :
    ListAdapter<CartEntity, CheckoutAdapter.ViewHolder>(callback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }

    inner class ViewHolder(val binding: ItemCheckoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartEntity, position: Int) {
            Glide.with(itemView.context)
                .load(item.image)
                .transform(CenterInside(), RoundedCorners(8))
                .into(binding.ivCheckout)

            binding.tvItemCheckoutName.text = item.productName
            binding.tvItemCheckoutVariant.text = item.variantName
            binding.tvItemCheckoutPrice.text = item.variantPrice?.toRupiah()
            binding.tvItemCheckoutQuantity.text = item.quantity.toString()
            if ((item.stock ?: 0) < 10) {
                binding.tvItemCheckoutStock.text =
                    itemView.context.getString(R.string.cart_remains).plus(" ${item.stock}")
                binding.tvItemCheckoutStock.setTextColor(Color.RED)
            } else {
                binding.tvItemCheckoutStock.text =
                    itemView.context.getString(R.string.all_stock).plus(" ${item.stock}")
            }

            binding.btnItemCheckoutToggleMin.setOnClickListener {
                if ((binding.tvItemCheckoutQuantity.text as String).toInt() > 1) {
                    binding.tvItemCheckoutQuantity.text =
                        (binding.tvItemCheckoutQuantity.text as String).toInt().minus(1).toString()
                    viewModel.updateCheckout(
                        cartEntity = CartEntity(
                            id = item.id,
                            image = item.image,
                            productName = item.productName,
                            productPrice = item.productPrice,
                            brand = item.brand,
                            store = item.store,
                            stock = item.stock,
                            variantPrice = item.variantPrice,
                            variantName = item.variantName,
                            quantity = (binding.tvItemCheckoutQuantity.text as String).toInt(),
                            isSelected = item.isSelected
                        ), position = position
                    )
                }
            }

            binding.btnItemCheckoutTogglePlus.setOnClickListener {
                if ((binding.tvItemCheckoutQuantity.text as String).toInt() < item.stock!!) {
                    binding.tvItemCheckoutQuantity.text =
                        (binding.tvItemCheckoutQuantity.text as String).toInt().plus(1).toString()
                    viewModel.updateCheckout(
                        cartEntity = CartEntity(
                            id = item.id,
                            image = item.image,
                            productName = item.productName,
                            productPrice = item.productPrice,
                            brand = item.brand,
                            store = item.store,
                            stock = item.stock,
                            variantPrice = item.variantPrice,
                            variantName = item.variantName,
                            quantity = (binding.tvItemCheckoutQuantity.text as String).toInt(),
                            isSelected = item.isSelected
                        ), position = position
                    )
                } else {
                    Snackbar.make(
                        itemView,
                        itemView.context.getString(R.string.all_stock_not_available),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

        }
    }

    companion object {
        val callback = object : DiffUtil.ItemCallback<CartEntity>() {
            override fun areItemsTheSame(oldItem: CartEntity, newItem: CartEntity): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: CartEntity, newItem: CartEntity): Boolean =
                oldItem.id == newItem.id
        }
    }
}