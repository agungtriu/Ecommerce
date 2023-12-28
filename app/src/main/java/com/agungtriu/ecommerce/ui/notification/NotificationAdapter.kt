package com.agungtriu.ecommerce.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.room.entity.NotificationEntity
import com.agungtriu.ecommerce.databinding.ItemNotificationBinding
import com.bumptech.glide.Glide

class NotificationAdapter(private val viewModel: NotificationViewModel) :
    ListAdapter<NotificationEntity, NotificationAdapter.ViewHolder>(callback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotificationEntity) {
            Glide.with(itemView.context)
                .load(item.image)
                .into(binding.ivItemNotification)

            binding.tvItemNotificationTitle.text = item.title
            binding.tvItemNotificationDatetime.text = item.date.plus(", ${item.time}")
            binding.tvItemNotificationDesc.text = item.body
            binding.tvItemNotificationType.text = item.type
            if (!item.isRead) {
                binding.constraintItemNotification.setBackgroundColor(
                    itemView.context.getColor(R.color.colorNotificationNotRead)
                )
                binding.constraintItemNotification.setOnClickListener {
                    viewModel.updateNotifications(
                        notificationEntity = NotificationEntity(
                            id = item.id,
                            title = item.title,
                            body = item.body,
                            image = item.image,
                            date = item.date,
                            time = item.time,
                            type = item.type,
                            isRead = true
                        )
                    )
                }
            }
        }
    }

    companion object {
        val callback = object : DiffUtil.ItemCallback<NotificationEntity>() {
            override fun areItemsTheSame(
                oldItem: NotificationEntity,
                newItem: NotificationEntity
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: NotificationEntity,
                newItem: NotificationEntity
            ): Boolean = oldItem.id == newItem.id
        }
    }
}
