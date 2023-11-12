package com.agungtriu.ecommerce.ui.main.wishlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.databinding.FragmentWishlistBinding
import com.agungtriu.ecommerce.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishlistFragment : BaseFragment<FragmentWishlistBinding>(FragmentWishlistBinding::inflate) {
    private val viewModel: WishlistViewModel by viewModels()
    private lateinit var adapter: WishlistAdapter
    private lateinit var gridLayoutManager: GridLayoutManager
    private var isGrid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getWishlists()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = WishlistAdapter(viewModel, viewLifecycleOwner, requireActivity())
        setLayout()
        listener()
        observeData()
    }

    private fun setLayout(viewType: Int = 1) {
        gridLayoutManager = GridLayoutManager(view?.context, viewType)
        binding.rvWishlist.layoutManager = gridLayoutManager
        binding.rvWishlist.adapter = adapter
        adapter.setItemViewType(viewType)
    }

    private fun listener() {
        binding.ibWishlistView.setOnClickListener {
            isGrid = !isGrid
            val position = gridLayoutManager.findFirstCompletelyVisibleItemPosition()
            if (isGrid) {
                setLayout(viewType = 2)
                binding.ibWishlistView.setBackgroundResource(R.drawable.ic_linear_view)
            } else {
                setLayout(viewType = 1)
                binding.ibWishlistView.setBackgroundResource(R.drawable.ic_grid_view)
            }
            gridLayoutManager.scrollToPosition(position)
        }
    }

    private fun observeData() {
        viewModel.resultWishlist.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.tvWishlistCount.text =
                    it.size.toString().plus(" ${getString(R.string.all_item)}")
                adapter.submitList(it)
            } else {
                binding.constraintWishlist.visibility = View.GONE
                binding.constraintWishlistError.visibility = View.VISIBLE
            }
        }
    }
}