package com.agungtriu.ecommerce.ui.main.wishlist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.databinding.FragmentWishlistBinding
import com.agungtriu.ecommerce.ui.base.BaseFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishlistFragment : BaseFragment<FragmentWishlistBinding>(FragmentWishlistBinding::inflate) {
    private val viewModel: WishlistViewModel by viewModels()
    private lateinit var adapter: WishlistAdapter
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var analytics: FirebaseAnalytics
    private var viewType = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getWishlists()
        analytics = Firebase.analytics
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = WishlistAdapter(viewModel, viewLifecycleOwner, requireActivity(), analytics)
        setLayout()
        listener()
        observeData()
    }

    private fun setLayout() {
        viewType = if (viewModel.isGrid) 2 else 1
        gridLayoutManager = GridLayoutManager(view?.context, viewType)
        binding.rvWishlist.layoutManager = gridLayoutManager
        binding.rvWishlist.adapter = adapter
        adapter.setItemViewType(viewType)
    }

    private fun listener() {
        binding.ibWishlistView.setOnClickListener {
            if (viewModel.isGrid) {
                analytics.logEvent("btn_wishlist_view_grid", null)
            } else {
                analytics.logEvent("btn_wishlist_view_linear", null)
            }
            viewModel.isGrid = !viewModel.isGrid
            val position = gridLayoutManager.findFirstCompletelyVisibleItemPosition()
            setLayout()
            gridLayoutManager.scrollToPosition(position)
            binding.ibWishlistView.setBackgroundResource(
                if (viewModel.isGrid) R.drawable.ic_linear_view else R.drawable.ic_grid_view
            )
        }
    }

    private fun observeData() {
        viewModel.resultWishlist.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                binding.tvWishlistCount.text =
                    it.size.toString().plus(" ${getString(R.string.all_item)}")
                adapter.submitList(it)
            } else {
                binding.constraintWishlist.isVisible = false
                binding.scrollviewWishlistError.isVisible = true
                binding.layoutWishlistError.btnErrorResetRefresh.isVisible = false
            }
        }
    }
}
