package com.agungtriu.ecommerce.ui.cart

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.databinding.ItemCartBinding
import com.agungtriu.ecommerce.helper.Extension.toRupiah
import com.agungtriu.ecommerce.ui.MainActivity
import com.agungtriu.ecommerce.ui.detail.DetailProductFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar

class CartAdapter(private val viewModel: CartViewModel, private val activity: FragmentActivity) :
    ListAdapter<CartEntity, CartAdapter.ViewHolder>(callback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartEntity) {
            Glide.with(itemView.context)
                .load(item.image)
                .transform(CenterInside(), RoundedCorners(8))
                .into(binding.ivCart)
            binding.tvItemCartName.text = item.productName
            binding.tvItemCartPrice.text = item.variantPrice?.toRupiah()
            binding.tvItemCartVariant.text = item.variantName
            if ((item.stock ?: 0) < 10) {
                binding.tvItemCartStock.text =
                    itemView.context.getString(R.string.cart_remains).plus(" ${item.stock}")
                binding.tvItemCartStock.setTextColor(Color.RED)
            } else {
                binding.tvItemCartStock.text =
                    itemView.context.getString(R.string.all_stock).plus(" ${item.stock}")
            }
            binding.tvItemCartQuantity.text = item.quantity.toString()
            binding.cbItemCart.isChecked = item.isSelected!!

            binding.btnItemCartDelete.setOnClickListener {
                viewModel.deleteCart(item)
            }

            binding.cbItemCart.setOnClickListener {
                viewModel.updateCart(
                    cartEntity = CartEntity(
                        id = item.id,
                        image = item.image,
                        productName = item.productName,
                        productPrice = item.productPrice,
                        store = item.store,
                        stock = item.stock,
                        variantPrice = item.variantPrice,
                        variantName = item.variantName,
                        quantity = item.quantity,
                        isSelected = !item.isSelected!!
                    )
                )
            }

            binding.btnItemCartToggleMin.setOnClickListener {
                if ((binding.tvItemCartQuantity.text as String).toInt() > 1) {
                    binding.tvItemCartQuantity.text =
                        (binding.tvItemCartQuantity.text as String).toInt().minus(1).toString()
                    viewModel.updateCart(
                        cartEntity = CartEntity(
                            id = item.id,
                            image = item.image,
                            productName = item.productName,
                            productPrice = item.productPrice,
                            store = item.store,
                            stock = item.stock,
                            variantPrice = item.variantPrice,
                            variantName = item.variantName,
                            quantity = (binding.tvItemCartQuantity.text as String).toInt(),
                            isSelected = item.isSelected
                        )
                    )
                }
            }

            binding.btnItemCartTogglePlus.setOnClickListener {
                if ((binding.tvItemCartQuantity.text as String).toInt() < item.stock!!) {
                    binding.tvItemCartQuantity.text =
                        (binding.tvItemCartQuantity.text as String).toInt().plus(1).toString()
                    viewModel.updateCart(
                        cartEntity = CartEntity(
                            id = item.id,
                            image = item.image,
                            productName = item.productName,
                            productPrice = item.productPrice,
                            store = item.store,
                            stock = item.stock,
                            variantPrice = item.variantPrice,
                            variantName = item.variantName,
                            quantity = (binding.tvItemCartQuantity.text as String).toInt(),
                            isSelected = item.isSelected
                        )
                    )
                } else {
                    Snackbar.make(
                        itemView,
                        itemView.context.getString(R.string.all_stock_not_available),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

            binding.constraintItemCart.setOnClickListener {
                val bundle = bundleOf(DetailProductFragment.PRODUCT_ID_KEY to item.id)
                (activity as MainActivity).toDetail(bundle)
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