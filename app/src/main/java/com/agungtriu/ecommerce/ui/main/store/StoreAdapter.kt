package com.agungtriu.ecommerce.ui.main.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.remote.model.response.Product
import com.agungtriu.ecommerce.databinding.ItemStoreGridBinding
import com.agungtriu.ecommerce.databinding.ItemStoreLinearBinding
import com.agungtriu.ecommerce.helper.Extension.toRupiah
import com.agungtriu.ecommerce.helper.Screen
import com.agungtriu.ecommerce.ui.AppActivity
import com.agungtriu.ecommerce.ui.detail.DetailProductFragment.Companion.FROM_KEY
import com.agungtriu.ecommerce.ui.detail.DetailProductFragment.Companion.PRODUCT_ID_KEY
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Param
import com.google.firebase.analytics.logEvent

class StoreAdapter(
    private val activity: FragmentActivity,
    private val analytics: FirebaseAnalytics
) :
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
                .placeholder(R.mipmap.ic_thumbnail)
                .into(binding.ivItemStoreLinear)

            binding.tvItemStoreLinearTitle.text = item.productName
            binding.tvItemStoreLinearPrice.text = item.productPrice?.toRupiah()
            binding.tvItemStoreLinearStore.text = item.store
            binding.tvItemStoreLinearRating.text = item.productRating.toString()
            binding.tvItemStoreLinearSold.text =
                itemView.context.getString(R.string.item_sold_title).plus(" ${item.sale}")

            binding.cvItemStoreLinear.setOnClickListener {
                if (item.productId != null) {
                    analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
                        param(
                            Param.ITEMS,
                            bundleOf(
                                Param.ITEM_ID to item.productId,
                                Param.ITEM_NAME to item.productName,
                                Param.ITEM_BRAND to item.brand,
                            )
                        )
                        param(Param.ITEM_LIST_NAME, "Store")
                    }

                    val bundle = bundleOf(
                        PRODUCT_ID_KEY to item.productId,
                        FROM_KEY to Screen.STORE.name
                    )
                    (activity as AppActivity).navigate(
                        R.id.action_global_to_detail_fragment_compose,
                        bundle
                    )
                }
            }
        }
    }

    private inner class ViewGridHolder(private val binding: ItemStoreGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            Glide.with(itemView.context)
                .load(item.image)
                .placeholder(R.mipmap.ic_thumbnail)
                .into(binding.ivItemStoreGrid)

            binding.tvItemStoreGridTitle.text = item.productName
            binding.tvItemStoreGridPrice.text = item.productPrice?.toRupiah()
            binding.tvItemStoreGridStore.text = item.store
            binding.tvItemStoreGridRating.text = item.productRating.toString()
            binding.tvItemStoreGridSold.text =
                itemView.context.getString(R.string.item_sold_title).plus(" ${item.sale}")

            binding.cvItemStoreGrid.setOnClickListener {
                if (item.productId != null) {
                    analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
                        param(
                            Param.ITEMS,
                            bundleOf(
                                Param.ITEM_ID to item.productId,
                                Param.ITEM_NAME to item.productName,
                                Param.ITEM_BRAND to item.brand
                            )
                        )
                        param(Param.ITEM_LIST_NAME, "Store")
                    }

                    val bundle = bundleOf(
                        PRODUCT_ID_KEY to item.productId,
                        FROM_KEY to Screen.STORE.name
                    )
                    (activity as AppActivity).navigate(
                        R.id.action_global_to_detail_fragment_compose,
                        bundle
                    )
                }
            }
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
