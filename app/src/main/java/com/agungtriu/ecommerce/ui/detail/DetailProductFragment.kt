package com.agungtriu.ecommerce.ui.detail

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.remote.model.response.DataDetailProduct
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.core.room.entity.WishlistEntity
import com.agungtriu.ecommerce.databinding.FragmentDetailProductBinding
import com.agungtriu.ecommerce.helper.Extension.toRupiah
import com.agungtriu.ecommerce.helper.Screen
import com.agungtriu.ecommerce.helper.Utils.displayPrice
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.AppActivity
import com.agungtriu.ecommerce.ui.base.BaseFragment
import com.agungtriu.ecommerce.ui.checkout.CheckoutFragment
import com.agungtriu.ecommerce.ui.detail.compose.intentShare
import com.agungtriu.ecommerce.ui.review.ReviewFragment.Companion.REVIEW_KEY
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Param
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailProductFragment :
    BaseFragment<FragmentDetailProductBinding>(FragmentDetailProductBinding::inflate) {

    private val viewModel: DetailProductViewModel by viewModels()
    private lateinit var adapter: DetailProductAdapter
    private lateinit var productDetail: DataDetailProduct
    private lateinit var chip: Chip
    private lateinit var analytics: FirebaseAnalytics
    private var wishlistState = false
    private var wishlistPress = false
    private lateinit var selectedChip: Chip

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productDetail = DataDetailProduct()
        analytics = Firebase.analytics
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = DetailProductAdapter()
        binding.vpDetailImage.adapter = adapter
        observeData()
        observeWishlist()
        observeCart()
        listener()
    }

    private fun observeData() {
        viewModel.resultDetail.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                    binding.pbDetail.isVisible = true
                    binding.scrollviewDetailError.isVisible = false
                    binding.constraintDetailContent.isVisible = false
                }

                is ViewState.Error -> {
                    binding.pbDetail.isVisible = false
                    binding.scrollviewDetailError.isVisible = true
                    binding.constraintDetailContent.isVisible = false
                }

                is ViewState.Success -> {
                    binding.pbDetail.isVisible = false
                    binding.scrollviewDetailError.isVisible = false
                    binding.constraintDetailContent.isVisible = true
                    productDetail = it.data
                    sliderImage(it.data.image!!)
                    chipVariant(it.data)
                    displayData(it.data)

                    analyticsViewItem(it.data)
                }
            }
        }
    }

    private fun observeWishlist() {
        viewModel.getWishlistByProductId().observe(viewLifecycleOwner) {
            if (it != null) {
                wishlistState = true
                if (wishlistPress) {
                    Snackbar.make(
                        requireView(),
                        getString(R.string.detail_success_add)
                            .plus(" ${productDetail.productName}")
                            .plus(" ${getString(R.string.detail_to_wishlist)}"),
                        Snackbar.LENGTH_LONG
                    ).show()
                } else if (viewModel.stateDetail == Screen.WISHLIST.name || viewModel.stateDetail == Screen.CART.name) {
                    viewModel.selectedVariantName = it.variantName ?: ""
                    viewModel.selectedVariantPrice = it.variantPrice ?: 0
                } else if (viewModel.stateDetail == Screen.STORE.name) {
                    viewModel.selectedVariantName =
                        productDetail.productVariant?.get(0)?.variantName ?: ""
                    viewModel.selectedVariantPrice = displayPrice(
                        basePrice = productDetail.productPrice,
                        variantPrice = productDetail.productVariant?.get(0)?.variantPrice
                    )
                }
                binding.ivDetailWishlist.setBackgroundResource(R.drawable.ic_favorite)
            } else {
                wishlistState = false
                binding.ivDetailWishlist.setBackgroundResource(R.drawable.ic_favorite_border)
                if (wishlistPress) {
                    Snackbar.make(
                        requireView(),
                        getString(R.string.detail_success_remove)
                            .plus(" ${productDetail.productName}")
                            .plus(" ${getString(R.string.detail_from_wishlist)}"),
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    viewModel.selectedVariantName =
                        productDetail.productVariant?.get(0)?.variantName ?: ""
                    viewModel.selectedVariantPrice = displayPrice(
                        basePrice = productDetail.productPrice,
                        variantPrice = productDetail.productVariant?.get(0)?.variantPrice
                    )
                }
            }
            wishlistPress = false
        }
    }

    private fun observeCart() {
        viewModel.resultAddCart.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> binding.pbDetail.visibility = View.VISIBLE
                is ViewState.Success -> {
                    binding.pbDetail.visibility = View.GONE
                    when (it.data) {
                        "cart" -> Snackbar.make(
                            requireView(),
                            getString(R.string.all_success_add_cart),
                            Snackbar.LENGTH_LONG
                        ).show()

                        "quantity" -> Snackbar.make(
                            requireView(),
                            getString(R.string.all_success_update_quantity),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    analyticsEvent(FirebaseAnalytics.Event.ADD_TO_CART)
                }

                is ViewState.Error -> {
                    binding.pbDetail.visibility = View.GONE
                    Snackbar.make(
                        requireView(),
                        getString(R.string.all_stock_not_available),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun listener() {
        binding.toolbarDetail.setNavigationOnClickListener {
            analytics.logEvent("btn_detail_back", null)
            findNavController().navigateUp()
        }

        binding.layoutDetailError.btnErrorResetRefresh.setOnClickListener {
            analytics.logEvent("btn_detail_error_refresh", null)
            viewModel.getProductById()
        }

        binding.chipgroupDetailVariant.setOnCheckedStateChangeListener { group, _ ->
            productDetail.productVariant?.forEach { variant ->
                selectedChip = group.findViewById(group.checkedChipId)
                if (selectedChip.text.toString() == variant.variantName) {
                    viewModel.selectedVariantName = variant.variantName ?: ""
                    viewModel.selectedVariantPrice =
                        displayPrice(productDetail.productPrice, variant.variantPrice)
                    binding.tvDetailPrice.text = viewModel.selectedVariantPrice.toRupiah()

                    analytics.logEvent("btn_detail_variant", null)
                    analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
                        param(
                            Param.ITEMS,
                            bundleOf(Param.ITEM_NAME to viewModel.selectedVariantName)
                        )
                        param(Param.ITEM_LIST_NAME, "Variant")
                    }
                }
            }
        }

        binding.ivDetailShare.setOnClickListener {
            analytics.logEvent("btn_detail_share", null)
            intentShare(requireContext(), productDetail)
        }

        binding.ivDetailWishlist.setOnClickListener {
            wishlistPress = true
            if (wishlistState) {
                analytics.logEvent("btn_detail_wishlist_delete", null)
                viewModel.deleteWishlistById(productDetail.productId!!)
            } else {
                analytics.logEvent("btn_detail_wishlist_insert", null)
                viewModel.insertWishlist(productDetail.toWishlist(viewModel))
                analyticsEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST)
            }
        }

        binding.btnDetailReviewShowAll.setOnClickListener {
            analytics.logEvent("btn_detail_review_show_all", null)
            findNavController().navigate(
                R.id.action_detailProductFragment_to_reviewFragment,
                bundleOf(REVIEW_KEY to productDetail.productId)
            )
        }

        binding.btnDetailCart.setOnClickListener {
            analytics.logEvent("btn_detail_cart", null)
            viewModel.addCart(productDetail.toCart(viewModel))
        }

        binding.btnDetailBuy.setOnClickListener {
            analytics.logEvent("btn_detail_buy", null)
            analyticsEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT)

            (requireActivity() as AppActivity).navigate(
                action = R.id.action_global_to_checkout_fragment,
                bundle = bundleOf(
                    CheckoutFragment.CHECKOUT_KEY to listOf(
                        productDetail.toCart(
                            viewModel
                        )
                    )
                )
            )
        }
    }

    private fun sliderImage(images: List<String>) {
        adapter.submitList(images)
        if (adapter.itemCount > 1) {
            TabLayoutMediator(binding.tlDetail, binding.vpDetailImage) { tab, _ ->
                tab.setCustomView(R.layout.custom_tab_indicator)
            }.attach()
            binding.vpDetailImage.setCurrentItem(viewModel.sliderPosition ?: 0, false)
        } else {
            binding.tlDetail.visibility = View.GONE
        }
    }

    private fun chipVariant(item: DataDetailProduct) {
        item.productVariant?.forEach { variant ->
            chip = Chip(requireActivity())
            chip.setTextAppearanceResource(R.style.Theme_Ecommerce_ChipGroup_Chip)
            chip.text = variant.variantName
            chip.isCheckable = true
            if (viewModel.selectedVariantName == "") {
                chip.isChecked = true
                viewModel.selectedVariantName = variant.variantName ?: ""
                viewModel.selectedVariantPrice = displayPrice(
                    item.productPrice, variant.variantPrice
                )
                binding.tvDetailPrice.text = viewModel.selectedVariantPrice.toRupiah()
            } else if (viewModel.selectedVariantName == variant.variantName) {
                chip.isChecked = true
                binding.tvDetailPrice.text = viewModel.selectedVariantPrice.toRupiah()
            }
            binding.chipgroupDetailVariant.addView(chip)
        }
    }

    private fun displayData(item: DataDetailProduct) {
        binding.tvDetailName.text = item.productName
        binding.tvDetailSold.text =
            getString(R.string.item_sold_title).plus(" ${item.sale}")
        binding.tvDetailRating.text =
            item.productRating.toString().plus("${item.totalRating}")
        binding.tvDetailDescriptionContent.text = item.description
        binding.tvDetailReviewRating.text = item.productRating.toString()
        binding.tvDetailReviewSatisfaction.text =
            item.totalSatisfaction.toString().plus(getString(R.string.detail_satisfaction_desc))
        binding.tvDetailReviewCount.text =
            item.totalRating.toString().plus(" ${getString(R.string.detail_rating_desc)}")
                .plus(" ${getString(R.string.detail_dot)} ")
                .plus("${item.totalReview} ${getString(R.string.detail_review_desc)}")
    }

    private fun analyticsViewItem(item: DataDetailProduct) {
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM) {
            param(
                Param.ITEMS,
                bundleOf(
                    Param.ITEM_ID to item.productId,
                    Param.ITEM_NAME to item.productName,
                    Param.ITEM_BRAND to item.brand,
                )
            )
            param(Param.CURRENCY, "Rp")
            param(Param.VALUE, item.productPrice!!.toDouble())
        }
    }

    private fun analyticsEvent(firebaseEvent: String) {
        analytics.logEvent(firebaseEvent) {
            param(
                Param.ITEMS,
                bundleOf(
                    Param.ITEM_ID to productDetail.productId,
                    Param.ITEM_NAME to productDetail.productName,
                    Param.ITEM_BRAND to productDetail.brand,
                    Param.ITEM_VARIANT to viewModel.selectedVariantName
                )
            )
            param(Param.CURRENCY, "Rp")
            param(Param.VALUE, viewModel.selectedVariantPrice.toDouble())
        }
    }

    private fun DataDetailProduct.toWishlist(viewModel: DetailProductViewModel): WishlistEntity {
        return WishlistEntity(
            id = this.productId ?: System.currentTimeMillis().toString(),
            image = this.image?.get(0),
            productName = this.productName,
            productPrice = this.productPrice,
            store = this.store,
            productRating = this.productRating,
            brand = this.brand,
            sale = this.sale,
            stock = this.stock,
            variantName = viewModel.selectedVariantName,
            variantPrice = viewModel.selectedVariantPrice,
        )
    }

    private fun DataDetailProduct.toCart(viewModel: DetailProductViewModel): CartEntity {
        return CartEntity(
            id = this.productId ?: System.currentTimeMillis().toString(),
            image = this.image?.get(0),
            productName = this.productName,
            productPrice = this.productPrice,
            brand = this.brand,
            store = this.store,
            stock = this.stock,
            variantPrice = viewModel.selectedVariantPrice,
            variantName = viewModel.selectedVariantName
        )
    }

    override fun onDestroyView() {
        viewModel.sliderPosition = binding.vpDetailImage.currentItem
        super.onDestroyView()
    }

    companion object {
        const val PRODUCT_ID_KEY = "productId"
        const val FROM_KEY = "from"
    }
}
