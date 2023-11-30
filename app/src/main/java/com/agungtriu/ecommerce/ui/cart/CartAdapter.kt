package com.agungtriu.ecommerce.ui.cart

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
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
import com.agungtriu.ecommerce.helper.Utils.rounded
import com.agungtriu.ecommerce.helper.Utils.warningStock
import com.agungtriu.ecommerce.ui.MainActivity
import com.agungtriu.ecommerce.ui.detail.DetailProductFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Param
import com.google.firebase.analytics.logEvent

class CartAdapter(
    private val viewModel: CartViewModel,
    private val activity: FragmentActivity,
    private val analytics: FirebaseAnalytics
) :
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
                .transform(CenterInside(), RoundedCorners(rounded))
                .into(binding.ivCart)
            binding.tvItemCartName.text = item.productName
            binding.tvItemCartPrice.text = item.variantPrice?.toRupiah()
            binding.tvItemCartVariant.text = item.variantName
            if ((item.stock ?: 0) < warningStock) {
                binding.tvItemCartStock.text =
                    itemView.context.getString(R.string.cart_remains).plus(" ${item.stock}")
                binding.tvItemCartStock.setTextColor(Color.RED)
            } else {
                binding.tvItemCartStock.text =
                    itemView.context.getString(R.string.all_stock).plus(" ${item.stock}")
            }
            binding.tvItemCartQuantity.text = item.quantity.toString()
            binding.cbItemCart.isChecked = item.isSelected!!

            listener(binding, item, itemView)
        }
    }

    private fun listener(binding: ItemCartBinding, item: CartEntity, itemView: View) {
        binding.btnItemCartDelete.setOnClickListener {
            viewModel.deleteCart(item)
            analyticsRemoveFromCart(item)
        }

        binding.cbItemCart.setOnClickListener {
            updateCart(item, binding, true)
        }

        binding.btnItemCartToggleMin.setOnClickListener {
            if ((binding.tvItemCartQuantity.text as String).toInt() > 1) {
                binding.tvItemCartQuantity.text =
                    (binding.tvItemCartQuantity.text as String).toInt().minus(1).toString()
                updateCart(item, binding, false)
            }
        }

        binding.btnItemCartTogglePlus.setOnClickListener {
            if ((binding.tvItemCartQuantity.text as String).toInt() < item.stock!!) {
                binding.tvItemCartQuantity.text =
                    (binding.tvItemCartQuantity.text as String).toInt().plus(1).toString()
                updateCart(item, binding, false)
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

    private fun analyticsRemoveFromCart(item: CartEntity) {
        analytics.logEvent(FirebaseAnalytics.Event.REMOVE_FROM_CART) {
            param(
                Param.ITEMS,
                bundleOf(
                    Param.ITEM_ID to item.id,
                    Param.ITEM_NAME to item.productName,
                    Param.ITEM_BRAND to item.brand,
                    Param.ITEM_VARIANT to item.variantName
                )
            )
            param(Param.CURRENCY, "Rp")
            param(Param.VALUE, item.variantPrice!!.toDouble())
        }
    }

    private fun updateCart(item: CartEntity, binding: ItemCartBinding, isCheckBox: Boolean) {
        viewModel.updateCart(
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
                quantity = (binding.tvItemCartQuantity.text as String).toInt(),
                isSelected = if (isCheckBox) !item.isSelected!! else item.isSelected
            )
        )
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
