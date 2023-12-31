package com.agungtriu.ecommerce.ui.main.wishlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.room.entity.WishlistEntity
import com.agungtriu.ecommerce.databinding.ItemWishlistGridBinding
import com.agungtriu.ecommerce.databinding.ItemWishlistLinearBinding
import com.agungtriu.ecommerce.helper.Extension.toCart
import com.agungtriu.ecommerce.helper.Extension.toRupiah
import com.agungtriu.ecommerce.helper.Screen
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.AppActivity
import com.agungtriu.ecommerce.ui.detail.DetailProductFragment.Companion.FROM_KEY
import com.agungtriu.ecommerce.ui.detail.DetailProductFragment.Companion.PRODUCT_ID_KEY
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Param
import com.google.firebase.analytics.logEvent

class WishlistAdapter(
    private val viewModel: WishlistViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val activity: FragmentActivity,
    private val analytics: FirebaseAnalytics
) :
    ListAdapter<WishlistEntity, RecyclerView.ViewHolder>(callback) {

    private var viewType = 1
    fun setItemViewType(viewType: Int) {
        this.viewType = viewType
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ONE) {
            return ViewLinearHolder(
                ItemWishlistLinearBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return ViewGridHolder(
                ItemWishlistGridBinding.inflate(
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

    inner class ViewGridHolder(private val binding: ItemWishlistGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WishlistEntity) {
            Glide.with(itemView.context).load(item.image)
                .placeholder(R.mipmap.ic_thumbnail)
                .into(binding.ivItemWishlistGrid)

            binding.tvItemWishlistGridPrice.text = item.variantPrice?.toRupiah()
            binding.tvItemWishlistGridTitle.text = item.productName
            binding.tvItemWishlistGridRating.text = item.productRating.toString()
            binding.tvItemWishlistGridSold.text =
                itemView.context.getString(R.string.item_sold_title).plus(" ${item.sale}")
            binding.tvItemWishlistGridStore.text = item.store
            listenerGrid(binding, item, itemView)
        }
    }

    inner class ViewLinearHolder(private val binding: ItemWishlistLinearBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WishlistEntity) {
            Glide.with(itemView.context)
                .load(item.image)
                .placeholder(R.mipmap.ic_thumbnail)
                .into(binding.ivItemWishlistLinear)

            binding.tvItemWishlistLinearPrice.text = item.variantPrice?.toRupiah()
            binding.tvItemWishlistLinearTitle.text = item.productName
            binding.tvItemWishlistLinearRating.text = item.productRating.toString()
            binding.tvItemWishlistLinearSold.text =
                itemView.context.getString(R.string.item_sold_title).plus(" ${item.sale}")
            binding.tvItemWishlistLinearStore.text = item.store
            listenerLinear(binding, item, itemView)
        }
    }

    private fun listenerGrid(
        binding: ItemWishlistGridBinding,
        item: WishlistEntity,
        itemView: View
    ) {
        binding.btnWishlistRemoveGrid.setOnClickListener {
            Snackbar.make(
                itemView,
                itemView.context.getString(R.string.detail_success_remove)
                    .plus(" ${item.productName} ")
                    .plus(itemView.context.getString(R.string.detail_from_wishlist)),
                Snackbar.LENGTH_LONG
            ).show()
            viewModel.deleteWishlistById(item.id)
            viewModel.getWishlists()
        }
        binding.btnWishlistCartGrid.setOnClickListener {
            viewModel.addCart(
                cartEntity = item.toCart()
            ).observe(lifecycleOwner) {
                when (it) {
                    is ViewState.Success -> {
                        analyticsAddToCart(item)
                        when (it.data) {
                            "cart" -> {
                                Snackbar.make(
                                    itemView,
                                    itemView.context.getString(R.string.succes_added)
                                        .plus(" ${item.productName} ")
                                        .plus(itemView.context.getString(R.string.to_cart)),
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }

                            "quantity" -> {
                                Snackbar.make(
                                    itemView,
                                    itemView.context.getString(R.string.all_success_update_quantity)
                                        .plus(" ${item.productName}"),
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    }

                    is ViewState.Error -> {
                        Snackbar.make(
                            itemView,
                            itemView.context.getString(R.string.all_stock_not_available),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    else -> {}
                }
            }
        }

        binding.cvItemWishlistGrid.setOnClickListener {
            val bundle = bundleOf(
                PRODUCT_ID_KEY to item.id,
                FROM_KEY to Screen.WISHLIST.name
            )
            (activity as AppActivity).navigate(
                R.id.action_global_to_detail_fragment_compose,
                bundle
            )
        }
    }

    private fun listenerLinear(
        binding: ItemWishlistLinearBinding,
        item: WishlistEntity,
        itemView: View
    ) {
        binding.btnWishlistRemoveLinear.setOnClickListener {
            Snackbar.make(
                itemView,
                itemView.context.getString(R.string.detail_success_remove)
                    .plus(" ${item.productName} ")
                    .plus(itemView.context.getString(R.string.detail_from_wishlist)),
                Snackbar.LENGTH_LONG
            ).show()
            viewModel.deleteWishlistById(item.id)
            viewModel.getWishlists()
        }
        binding.btnWishlistCartLinear.setOnClickListener {
            viewModel.addCart(
                cartEntity = item.toCart()
            ).observe(lifecycleOwner) {
                when (it) {
                    is ViewState.Success -> {
                        analyticsAddToCart(item)
                        when (it.data) {
                            "cart" -> {
                                Snackbar.make(
                                    itemView,
                                    itemView.context.getString(R.string.succes_added)
                                        .plus(" ${item.productName} ")
                                        .plus(itemView.context.getString(R.string.to_cart)),
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }

                            "quantity" -> {
                                Snackbar.make(
                                    itemView,
                                    itemView.context.getString(R.string.all_success_update_quantity)
                                        .plus(" ${item.productName}"),
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    }

                    is ViewState.Error -> {
                        Snackbar.make(
                            itemView,
                            itemView.context.getString(R.string.all_stock_not_available),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    else -> {}
                }
            }
        }

        binding.cvItemWishlistLinear.setOnClickListener {
            val bundle = bundleOf(
                PRODUCT_ID_KEY to item.id,
                FROM_KEY to Screen.WISHLIST.name
            )
            (activity as AppActivity).navigate(
                R.id.action_global_to_detail_fragment_compose,
                bundle
            )
        }
    }

    private fun analyticsAddToCart(item: WishlistEntity) {
        analytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART) {
            param(
                Param.ITEMS,
                bundleOf(
                    Param.ITEM_ID to item.id,
                    Param.ITEM_NAME to item.productName,
                    Param.ITEM_VARIANT to item.variantName,
                    Param.ITEM_BRAND to item.brand
                )
            )
            param(Param.CURRENCY, "Rp")
            param(Param.VALUE, item.variantPrice!!.toDouble())
        }
    }

    companion object {
        val callback = object : DiffUtil.ItemCallback<WishlistEntity>() {
            override fun areItemsTheSame(
                oldItem: WishlistEntity,
                newItem: WishlistEntity
            ): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: WishlistEntity,
                newItem: WishlistEntity
            ): Boolean =
                oldItem.id == newItem.id
        }

        const val VIEW_TYPE_ONE = 1
    }
}
