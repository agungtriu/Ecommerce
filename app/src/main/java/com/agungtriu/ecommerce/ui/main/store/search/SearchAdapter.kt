package com.agungtriu.ecommerce.ui.main.store.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.findFragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agungtriu.ecommerce.databinding.ItemSearchBinding
import com.agungtriu.ecommerce.ui.main.store.search.SearchDialog.Companion.RESULT_SEARCH_KEY
import com.agungtriu.ecommerce.ui.main.store.search.SearchDialog.Companion.SEARCH_KEY

class SearchAdapter :
    ListAdapter<String, SearchAdapter.ViewHolder>(callback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.tvItemSearch.text = item
            binding.tvItemSearch.setOnClickListener {
                itemView.findFragment<SearchDialog>().setFragmentResult(
                    SEARCH_KEY,
                    bundleOf(RESULT_SEARCH_KEY to binding.tvItemSearch.text)
                )
                itemView.findFragment<SearchDialog>().dismiss()
            }
        }
    }

    companion object {
        val callback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String) =
                oldItem == newItem
        }
    }
}
