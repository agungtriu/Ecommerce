package com.agungtriu.ecommerce.ui.main.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.remote.model.response.DataTransaction
import com.agungtriu.ecommerce.databinding.ItemTransactionBinding
import com.agungtriu.ecommerce.helper.Extension.toRupiah
import com.agungtriu.ecommerce.helper.Utils.rounded
import com.agungtriu.ecommerce.ui.MainActivity
import com.agungtriu.ecommerce.ui.status.StatusFragment
import com.agungtriu.ecommerce.ui.status.StatusFragment.Companion.STATE_STATUS_KEY
import com.agungtriu.ecommerce.ui.status.StatusModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TransactionAdapter(private val activity: FragmentActivity) :
    ListAdapter<DataTransaction, TransactionAdapter.ViewHolder>(callback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataTransaction) {
            Glide.with(itemView.context)
                .load(item.image).transform(CenterInside(), RoundedCorners(rounded))
                .placeholder(R.mipmap.ic_thumbnail)
                .into(binding.ivItemTransaction)

            binding.tvItemTransactionDate.text = item.date
            binding.tvItemTransactionStatus.text =
                if (item.status == true) {
                    itemView.context.getString(R.string.all_done)
                } else {
                    itemView.context.getString(R.string.all_payment)
                }
            binding.tvItemTransactionProductName.text = item.name
            binding.tvItemTransactionQuantity.text = item.items?.size.toString()
                .plus(" ${itemView.context.getString(R.string.all_item)}")
            binding.tvItemTransactionTotalShopping.text = item.total?.toRupiah()
            binding.btnItemTransactionReview.isVisible = item.review == null || item.rating == null
            binding.btnItemTransactionReview.setOnClickListener {
                val bundle = bundleOf(
                    StatusFragment.STATUS_KEY to StatusModel(
                        invoiceId = item.invoiceId,
                        date = item.date,
                        total = item.total,
                        payment = item.payment,
                        time = item.time,
                        status = item.status,
                        rating = item.rating,
                        review = item.review
                    ),
                    STATE_STATUS_KEY to "transaction"
                )
                (activity as MainActivity).toStatus(bundle)
            }
        }
    }

    companion object {
        val callback = object : DiffUtil.ItemCallback<DataTransaction>() {
            override fun areItemsTheSame(
                oldItem: DataTransaction,
                newItem: DataTransaction
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: DataTransaction,
                newItem: DataTransaction
            ): Boolean = oldItem.invoiceId == newItem.invoiceId
        }
    }
}
