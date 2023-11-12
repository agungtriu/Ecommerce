package com.agungtriu.ecommerce.ui.main.wishlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.core.room.entity.WishlistEntity
import com.agungtriu.ecommerce.databinding.ItemWishlistGridBinding
import com.agungtriu.ecommerce.databinding.ItemWishlistLinearBinding
import com.agungtriu.ecommerce.helper.Extension.toRupiah
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.MainActivity
import com.agungtriu.ecommerce.ui.detail.DetailProductFragment
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

class WishlistAdapter(
    private val viewModel: WishlistViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val activity: FragmentActivity
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
            Glide.with(itemView.context).load(item.image).placeholder(R.mipmap.ic_thumbnail)
                .into(binding.ivItemWishlistGrid)

            binding.tvItemWishlistGridPrice.text = item.productPrice?.toRupiah()
            binding.tvItemWishlistGridTitle.text = item.productName
            binding.tvItemWishlistGridRating.text = item.productRating.toString()
            binding.tvItemWishlistGridSold.text =
                itemView.context.getString(R.string.item_sold_title).plus(" ${item.sale}")
            binding.tvItemWishlistGridStore.text = item.store
            binding.btnWishlistRemoveGrid.setOnClickListener {
                viewModel.deleteWishlistById(item.id)
                viewModel.getWishlists()
            }
            binding.btnWishlistCartGrid.setOnClickListener {
                viewModel.addCart(
                    cartEntity = CartEntity(
                        id = item.id,
                        image = item.image,
                        productName = item.productName,
                        productPrice = item.productPrice,
                        store = item.store,
                        stock = item.stock,
                        variantPrice = item.variantPrice,
                        variantName = item.variantName
                    )
                ).observe(lifecycleOwner) {
                    when (it) {
                        is ViewState.Success -> {
                            when (it.data) {
                                "cart" -> {
                                    Snackbar.make(
                                        itemView,
                                        itemView.context.getString(R.string.all_success_add_cart),
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }

                                "quantity" -> {
                                    Snackbar.make(
                                        itemView,
                                        itemView.context.getString(R.string.all_success_update_quantity),
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
                val bundle = bundleOf(DetailProductFragment.PRODUCT_ID_KEY to item.id)
                (activity as MainActivity).toDetail(bundle)
            }
        }
    }

    inner class ViewLinearHolder(private val binding: ItemWishlistLinearBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WishlistEntity) {
            Glide.with(itemView.context).load(item.image).placeholder(R.mipmap.ic_thumbnail)
                .into(binding.ivItemWishlistLinear)

            binding.tvItemWishlistLinearPrice.text = item.productPrice?.toRupiah()
            binding.tvItemWishlistLinearTitle.text = item.productName
            binding.tvItemWishlistLinearRating.text = item.productRating.toString()
            binding.tvItemWishlistLinearSold.text =
                itemView.context.getString(R.string.item_sold_title).plus(" ${item.sale}")
            binding.tvItemWishlistLinearStore.text = item.store
            binding.btnWishlistRemoveLinear.setOnClickListener {
                viewModel.deleteWishlistById(item.id)
                viewModel.getWishlists()
            }
            binding.btnWishlistCartLinear.setOnClickListener {
                viewModel.addCart(
                    cartEntity = CartEntity(
                        id = item.id,
                        image = item.image,
                        productName = item.productName,
                        productPrice = item.productPrice,
                        store = item.store,
                        stock = item.stock,
                        variantPrice = item.variantPrice,
                        variantName = item.variantName
                    )
                ).observe(lifecycleOwner) {
                    when (it) {
                        is ViewState.Success -> {
                            when (it.data) {
                                "cart" -> {
                                    Snackbar.make(
                                        itemView,
                                        itemView.context.getString(R.string.all_success_add_cart),
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }

                                "quantity" -> {
                                    Snackbar.make(
                                        itemView,
                                        itemView.context.getString(R.string.all_success_update_quantity),
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
                val bundle = bundleOf(DetailProductFragment.PRODUCT_ID_KEY to item.id)
                (activity as MainActivity).toDetail(bundle)
            }
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