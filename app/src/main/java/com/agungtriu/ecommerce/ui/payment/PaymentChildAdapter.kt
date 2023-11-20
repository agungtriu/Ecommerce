package com.agungtriu.ecommerce.ui.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agungtriu.ecommerce.core.remote.model.response.DataPayment
import com.agungtriu.ecommerce.databinding.ItemPaymentChildBinding
import com.agungtriu.ecommerce.ui.checkout.CheckoutFragment.Companion.RESULT_FROM_PAYMENT
import com.bumptech.glide.Glide

class PaymentChildAdapter(
    private val navController: NavController
) :
    ListAdapter<DataPayment, PaymentChildAdapter.ViewHolder>(callback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemPaymentChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemPaymentChildBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataPayment) {
            binding.tvItemPaymentChild.text = item.label
            Glide.with(itemView.context)
                .load(item.image)
                .into(binding.ivItemPaymentChild)


            if (item.status != true) {
                binding.constraintItemPaymentChild.isClickable = false
                binding.constraintItemPaymentChild.isEnabled = false
                binding.constraintItemPaymentChild.alpha = 0.5f
            } else {
                binding.constraintItemPaymentChild.setOnClickListener {
                    val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
                    savedStateHandle?.set(RESULT_FROM_PAYMENT, item)
                    navController.navigateUp()
                }
            }
        }
    }

    companion object {
        val callback = object : DiffUtil.ItemCallback<DataPayment>() {
            override fun areItemsTheSame(oldItem: DataPayment, newItem: DataPayment): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: DataPayment, newItem: DataPayment): Boolean =
                oldItem.label == newItem.label

        }
    }
}