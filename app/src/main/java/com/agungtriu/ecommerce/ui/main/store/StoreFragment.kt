package com.agungtriu.ecommerce.ui.main.store

import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.remote.model.request.RequestProducts
import com.agungtriu.ecommerce.core.remote.model.response.ResponseError
import com.agungtriu.ecommerce.databinding.FragmentStoreBinding
import com.agungtriu.ecommerce.helper.Extension.toResponseError
import com.agungtriu.ecommerce.helper.Extension.toRupiah
import com.agungtriu.ecommerce.ui.base.BaseFragment
import com.agungtriu.ecommerce.ui.main.store.filter.FilterBottomSheet
import com.agungtriu.ecommerce.ui.main.store.filter.FilterBottomSheet.Companion.FILTER_KEY
import com.agungtriu.ecommerce.ui.main.store.filter.FilterBottomSheet.Companion.RESULT_FILTER_KEY
import com.agungtriu.ecommerce.ui.main.store.filter.FilterModel
import com.agungtriu.ecommerce.ui.main.store.search.SearchDialog
import com.agungtriu.ecommerce.ui.main.store.search.SearchDialog.Companion.RESULT_SEARCH_KEY
import com.agungtriu.ecommerce.ui.main.store.search.SearchDialog.Companion.SEARCH_KEY
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StoreFragment : BaseFragment<FragmentStoreBinding>(FragmentStoreBinding::inflate) {
    private val viewModel: StoreVideModel by viewModels()

    private lateinit var storeAdapter: StoreAdapter
    private lateinit var searchDialog: SearchDialog
    private lateinit var filterBottomSheet: FilterBottomSheet
    private lateinit var gridLayoutManager: GridLayoutManager
    private var requestProducts = RequestProducts()
    private var filterModel = FilterModel()
    private val bundle = Bundle()
    private var isGrid = false
    private lateinit var error: ResponseError
    private lateinit var chip: Chip
    private var querySearch: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storeAdapter = StoreAdapter()
        setLayout()
        listener()
        resultListener()
        getProducts()
        observeState()
    }

    private fun setLayout(viewType: Int = 1) {
        gridLayoutManager = GridLayoutManager(view?.context, viewType)
        binding.rvStore.layoutManager = gridLayoutManager
        storeAdapter.setItemViewType(viewType)
        binding.rvStore.adapter = storeAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storeAdapter.retry()
            }
        )

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position < storeAdapter.itemCount) 1 else gridLayoutManager.spanCount
            }
        }
    }

    private fun listener() {
        binding.ibStoreView.setOnClickListener {
            isGrid = !isGrid
            val position = gridLayoutManager.findFirstVisibleItemPosition()
            if (isGrid) {
                setLayout(viewType = 2)
                binding.ibStoreView.setBackgroundResource(R.drawable.ic_linear_view)
            } else {
                setLayout(viewType = 1)
                binding.ibStoreView.setBackgroundResource(R.drawable.ic_grid_view)
            }
            gridLayoutManager.scrollToPosition(position)
        }

        binding.swiperStore.setOnRefreshListener {
            getProducts(requestProducts = requestProducts)
        }

        binding.btnStoreErrorReset.setOnClickListener {
            binding.tietStoreSearch.text = null
            binding.chipgroupBottomshettfilter.removeAllViews()
            requestProducts = RequestProducts()
            getProducts()
        }

        binding.btnStoreErrorRefresh.setOnClickListener {
            getProducts(requestProducts)
        }

        binding.chipStoreFilter.setOnClickListener {
            filterModel = FilterModel(
                sort = requestProducts.sort,
                category = requestProducts.brand,
                min = requestProducts.lowest,
                max = requestProducts.highest
            )
            bundle.putParcelable(
                TO_FILTER_KEY, filterModel
            )
            filterBottomSheet = FilterBottomSheet()
            filterBottomSheet.arguments = bundle
            filterBottomSheet.show(childFragmentManager, FilterBottomSheet.TAG)
        }

        binding.tietStoreSearch.setOnClickListener {
            searchDialog = SearchDialog()
            bundle.putString(TO_SEARCH_KEY, requestProducts.search)
            searchDialog.arguments = bundle
            searchDialog.show(childFragmentManager, SearchDialog.TAG)
        }

    }

    private fun resultListener() {
        childFragmentManager.setFragmentResultListener(
            FILTER_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            binding.chipgroupBottomshettfilter.removeAllViews()
            filterModel = if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(RESULT_FILTER_KEY, FilterModel::class.java)
            } else {
                bundle.getParcelable(RESULT_FILTER_KEY)
            } ?: FilterModel()

            addChip(
                !filterModel.sort.isNullOrBlank(),
                filterModel.sort
            )
            addChip(
                !filterModel.category.isNullOrBlank(),
                filterModel.category
            )
            addChip(
                filterModel.min != null,
                "> ".plus(filterModel.min?.toRupiah())
            )
            addChip(
                filterModel.max != null,
                "< ".plus(filterModel.max?.toRupiah())
            )
            requestProducts = RequestProducts(
                search = requestProducts.search,
                sort = filterModel.sort,
                brand = filterModel.category,
                lowest = filterModel.min,
                highest = filterModel.max
            )
            getProducts(requestProducts)
        }

        childFragmentManager.setFragmentResultListener(
            SEARCH_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            querySearch = bundle.getString(RESULT_SEARCH_KEY)
            binding.tietStoreSearch.setText(querySearch)
            requestProducts = RequestProducts(
                search = querySearch,
                sort = filterModel.sort,
                brand = filterModel.category,
                lowest = filterModel.min,
                highest = filterModel.max
            )
            getProducts(requestProducts)
        }
    }

    private fun addChip(condition: Boolean, text: String?) {
        if (condition) {
            chip = Chip(requireActivity())
            chip.text = text
            binding.chipgroupBottomshettfilter.addView(chip)
        }
    }

    private fun getProducts(requestProducts: RequestProducts = RequestProducts()) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getProducts(requestProducts).collect {
                storeAdapter.submitData(lifecycle, it)
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            storeAdapter.loadStateFlow.collectLatest { loadState ->
                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        showShimmer()
                        binding.constraintStore.visibility = View.GONE
                        binding.constraintError.visibility = View.GONE
                    }

                    is LoadState.NotLoading -> {
                        hideShimmer()
                        binding.swiperStore.isRefreshing = false
                        binding.constraintStore.visibility = View.VISIBLE
                    }

                    is LoadState.Error -> {
                        hideShimmer()
                        binding.swiperStore.isRefreshing = false
                        binding.constraintError.visibility = View.VISIBLE
                        error = (loadState.refresh as LoadState.Error).error.toResponseError()
                        when (error.code) {
                            404 -> {
                                binding.btnStoreErrorRefresh.visibility = View.GONE
                                binding.btnStoreErrorReset.visibility = View.VISIBLE
                                binding.tvStoreErrorTitle.text = getString(R.string.store_empty)
                                binding.tvStoreErrorDesc.text = getString(R.string.store_empty_desc)
                            }

                            503 -> {
                                binding.btnStoreErrorRefresh.visibility = View.VISIBLE
                                binding.btnStoreErrorReset.visibility = View.GONE
                                binding.tvStoreErrorTitle.text =
                                    getString(R.string.store_connection_title)
                                binding.tvStoreErrorDesc.text =
                                    getString(R.string.store_connection_desc)
                            }

                            else -> {
                                binding.btnStoreErrorRefresh.visibility = View.VISIBLE
                                binding.btnStoreErrorReset.visibility = View.GONE
                                binding.tvStoreErrorTitle.text =
                                    if (error.code != null) error.code.toString() else null
                                binding.tvStoreErrorDesc.text = error.message

                            }
                        }
                    }
                }
            }
        }
    }

    private fun showShimmer() {
        if (isGrid) {
            binding.shimmerStoreGrid.visibility = View.VISIBLE
            binding.shimmerStoreGrid.startShimmer()
        } else {
            binding.shimmerStoreLinear.visibility = View.VISIBLE
            binding.shimmerStoreLinear.startShimmer()
        }
        binding.shimmerStoreFilter.visibility = View.VISIBLE
    }

    private fun hideShimmer() {
        if (isGrid) {
            binding.shimmerStoreGrid.stopShimmer()
            binding.shimmerStoreGrid.visibility = View.GONE
        } else {
            binding.shimmerStoreLinear.stopShimmer()
            binding.shimmerStoreLinear.visibility = View.GONE
        }
        binding.shimmerStoreFilter.visibility = View.GONE
    }

    companion object {
        const val TO_FILTER_KEY = "FilterKey"
        const val TO_SEARCH_KEY = "SearchKey"
    }
}