package com.agungtriu.ecommerce.ui.main.store

import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.remote.model.request.RequestProducts
import com.agungtriu.ecommerce.core.remote.model.response.ResponseError
import com.agungtriu.ecommerce.databinding.FragmentStoreBinding
import com.agungtriu.ecommerce.helper.Extension.toFilterModel
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
    private var filterModel = FilterModel()
    private var bundle = Bundle()
    private lateinit var error: ResponseError
    private lateinit var chip: Chip
    private var querySearch: String? = null
    private var viewType: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storeAdapter = StoreAdapter(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        listener()
        resultListener()
        observeData()
        observeState()
        observeFilter(filterModel = viewModel.requestProducts.toFilterModel())
    }

    private fun setLayout() {
        viewType = if (viewModel.isGrid) 2 else 1

        gridLayoutManager = GridLayoutManager(view?.context, viewType)
        binding.rvStore.layoutManager = gridLayoutManager
        storeAdapter.setItemViewType(viewType)
        binding.rvStore.adapter = storeAdapter.withLoadStateFooter(footer = LoadingStateAdapter {
            storeAdapter.retry()
        })

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position < storeAdapter.itemCount) 1 else gridLayoutManager.spanCount
            }
        }
    }

    private fun listener() {
        binding.ibStoreView.setOnClickListener {
            viewModel.isGrid = !viewModel.isGrid
            val position = gridLayoutManager.findFirstCompletelyVisibleItemPosition()
            setLayout()
            gridLayoutManager.scrollToPosition(position)
            binding.ibStoreView.setBackgroundResource(if (viewModel.isGrid) R.drawable.ic_linear_view else R.drawable.ic_grid_view)
        }

        binding.swiperStore.setOnRefreshListener {
            viewModel.getProducts(viewModel.requestProducts)
        }

        binding.layoutStoreError.btnErrorResetRefresh.setOnClickListener {
            if (binding.layoutStoreError.btnErrorResetRefresh.text == getString(R.string.all_reset)) {
                binding.tietStoreSearch.text = null
                binding.chipgroupBottomshettfilter.removeAllViews()
                viewModel.requestProducts = RequestProducts()
            }
            viewModel.getProducts(viewModel.requestProducts)
        }

        binding.chipStoreFilter.setOnClickListener {
            filterModel = viewModel.requestProducts.toFilterModel()
            bundle = bundleOf(
                TO_FILTER_KEY to filterModel
            )
            filterBottomSheet = FilterBottomSheet()
            filterBottomSheet.arguments = bundle
            filterBottomSheet.show(childFragmentManager, FilterBottomSheet.TAG)
        }

        binding.tietStoreSearch.setOnClickListener {
            searchDialog = SearchDialog()
            bundle = bundleOf(TO_SEARCH_KEY to viewModel.requestProducts.search)
            searchDialog.arguments = bundle
            searchDialog.show(childFragmentManager, SearchDialog.TAG)
        }
    }

    private fun resultListener() {
        childFragmentManager.setFragmentResultListener(
            FILTER_KEY, viewLifecycleOwner
        ) { _, bundle ->
            filterModel = if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(RESULT_FILTER_KEY, FilterModel::class.java)
            } else {
                bundle.getParcelable(RESULT_FILTER_KEY)
            } ?: FilterModel()

            observeFilter(filterModel)

            viewModel.requestProducts = RequestProducts(
                search = viewModel.requestProducts.search,
                sort = filterModel.sort,
                brand = filterModel.category,
                lowest = filterModel.min,
                highest = filterModel.max
            )
            viewModel.getProducts(viewModel.requestProducts)
        }

        childFragmentManager.setFragmentResultListener(
            SEARCH_KEY, viewLifecycleOwner
        ) { _, bundle ->
            querySearch = bundle.getString(RESULT_SEARCH_KEY)
            binding.tietStoreSearch.setText(querySearch)
            viewModel.requestProducts = RequestProducts(
                search = querySearch,
                sort = viewModel.requestProducts.sort,
                brand = viewModel.requestProducts.brand,
                lowest = viewModel.requestProducts.lowest,
                highest = viewModel.requestProducts.highest
            )
            viewModel.getProducts(viewModel.requestProducts)
        }
    }

    private fun addChip(condition: Boolean, text: String?) {
        if (condition) {
            chip = Chip(requireActivity())
            chip.setTextAppearanceResource(R.style.Theme_Ecommerce_ChipGroup_Chip)
            chip.text = text
            binding.chipgroupBottomshettfilter.addView(chip)
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.resultProducts.observe(viewLifecycleOwner) {
                storeAdapter.submitData(lifecycle, it)
            }
        }
    }

    private fun observeFilter(filterModel: FilterModel) {
        binding.chipgroupBottomshettfilter.removeAllViews()
        addChip(
            !filterModel.sort.isNullOrBlank(), filterModel.sort
        )
        addChip(
            !filterModel.category.isNullOrBlank(), filterModel.category
        )
        addChip(
            filterModel.min != null, "> ".plus(filterModel.min?.toRupiah())
        )
        addChip(
            filterModel.max != null, "< ".plus(filterModel.max?.toRupiah())
        )
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            storeAdapter.loadStateFlow.collectLatest { loadState ->
                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        showShimmer()
                        binding.constraintStore.visibility = View.GONE
                        binding.scrollviewStoreError.visibility = View.GONE
                    }

                    is LoadState.NotLoading -> {
                        hideShimmer()
                        binding.swiperStore.isRefreshing = false
                        binding.constraintStore.visibility = View.VISIBLE
                    }

                    is LoadState.Error -> {
                        hideShimmer()
                        binding.constraintStore.visibility = View.GONE
                        binding.swiperStore.isRefreshing = false
                        binding.scrollviewStoreError.visibility = View.VISIBLE
                        error = (loadState.refresh as LoadState.Error).error.toResponseError()
                        if (error.message != null) {
                            viewModel.error = error
                        }
                        when (viewModel.error.code) {
                            404 -> {
                                binding.layoutStoreError.btnErrorResetRefresh.text =
                                    getString(R.string.all_reset)
                                binding.layoutStoreError.tvErrorTitle.text =
                                    getString(R.string.store_empty)
                                binding.layoutStoreError.tvErrorDesc.text =
                                    getString(R.string.store_empty_desc)
                            }

                            503 -> {
                                binding.layoutStoreError.btnErrorResetRefresh.text =
                                    getString(R.string.all_refresh)
                                binding.layoutStoreError.tvErrorTitle.text =
                                    getString(R.string.store_connection_title)
                                binding.layoutStoreError.tvErrorDesc.text =
                                    getString(R.string.store_connection_desc)
                            }

                            else -> {
                                binding.layoutStoreError.btnErrorResetRefresh.text =
                                    getString(R.string.all_refresh)
                                binding.layoutStoreError.tvErrorTitle.text =
                                    if (viewModel.error.code != null) viewModel.error.code.toString() else null
                                binding.layoutStoreError.tvErrorDesc.text =
                                    viewModel.error.message
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showShimmer() {
        if (viewModel.isGrid) {
            binding.shimmerStoreGrid.visibility = View.VISIBLE
            binding.shimmerStoreGrid.startShimmer()
        } else {
            binding.shimmerStoreLinear.visibility = View.VISIBLE
            binding.shimmerStoreLinear.startShimmer()
        }
        binding.shimmerStoreFilter.visibility = View.VISIBLE
    }

    private fun hideShimmer() {
        if (viewModel.isGrid) {
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