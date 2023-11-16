package com.agungtriu.ecommerce.ui.prelogin.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agungtriu.ecommerce.databinding.ItemImageBinding

class OnboardingAdapter : ListAdapter<Int, OnboardingAdapter.ViewHolder>(callback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(
        ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Int) {
            binding.ivItemImage.setImageResource(item)
        }
    }

    companion object {
        val callback = object : DiffUtil.ItemCallback<Int>() {
            override fun areItemsTheSame(oldItem: Int, newItem: Int) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Int, newItem: Int) =
                oldItem == newItem
        }
    }
}