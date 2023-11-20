package com.agungtriu.ecommerce.ui.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.remote.model.response.DataReview
import com.agungtriu.ecommerce.databinding.ItemReviewBinding
import com.bumptech.glide.Glide


class ReviewAdapter : ListAdapter<DataReview, ReviewAdapter.ViewHolder>(callback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataReview) {

            Glide.with(itemView.context)
                .load(item.userImage)
                .placeholder(R.drawable.ic_account_outline_circle)
                .circleCrop()
                .into(binding.ivItemReview)

            binding.tvItemReviewName.text = item.userName
            binding.tvItemReviewRating.rating = item.userRating!!
            binding.tvReviewDesc.text = item.userReview
        }
    }

    companion object {
        val callback = object : DiffUtil.ItemCallback<DataReview>() {
            override fun areItemsTheSame(oldItem: DataReview, newItem: DataReview): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: DataReview, newItem: DataReview): Boolean =
                oldItem.userReview == newItem.userReview

        }
    }
}