package com.agungtriu.ecommerce.ui.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agungtriu.ecommerce.core.remote.model.response.DataTypePayment
import com.agungtriu.ecommerce.databinding.ItemPaymentParentBinding

class PaymentParentAdapter(private val navController: NavController) :
    ListAdapter<DataTypePayment, PaymentParentAdapter.ViewHolder>(callback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemPaymentParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemPaymentParentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataTypePayment) {
            val adapter = PaymentChildAdapter(navController)
            binding.rvItemPaymentParent.layoutManager = LinearLayoutManager(itemView.context)
            binding.rvItemPaymentParent.adapter = adapter
            binding.tvItemPaymentParentTitle.text = item.title
            adapter.submitList(item.item)
        }
    }

    companion object {
        val callback = object : DiffUtil.ItemCallback<DataTypePayment>() {
            override fun areItemsTheSame(
                oldItem: DataTypePayment,
                newItem: DataTypePayment
            ): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: DataTypePayment,
                newItem: DataTypePayment
            ): Boolean = oldItem.title == newItem.title
        }
    }
}
