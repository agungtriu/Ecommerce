package com.agungtriu.ecommerce.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.fragment.findNavController
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.remote.model.response.DataDetailProduct
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.core.room.entity.WishlistEntity
import com.agungtriu.ecommerce.databinding.FragmentDetailProductBinding
import com.agungtriu.ecommerce.helper.Config.BASE_DEEPLINK
import com.agungtriu.ecommerce.helper.Extension.toRupiah
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.MainActivity
import com.agungtriu.ecommerce.ui.base.BaseFragment
import com.agungtriu.ecommerce.ui.checkout.CheckoutFragment
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
    private lateinit var bundle: Bundle
    private lateinit var analytics: FirebaseAnalytics
    private var wishlistState = false
    private var wishlistPress = false
    private lateinit var selectedChip: Chip

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        listener()
    }

    @SuppressLint("SetTextI18n")
    private fun observeData() {
        viewModel.resultDetail.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                    binding.pbDetail.visibility = View.VISIBLE
                    binding.constraintDetailContent.visibility = View.GONE
                    binding.scrollviewDetailError.visibility = View.GONE
                }

                is ViewState.Error -> {
                    binding.pbDetail.visibility = View.GONE
                    binding.constraintDetailContent.visibility = View.GONE
                    binding.scrollviewDetailError.visibility = View.VISIBLE
                }

                is ViewState.Success -> {
                    productDetail = it.data
                    binding.pbDetail.visibility = View.GONE
                    binding.scrollviewDetailError.visibility = View.GONE
                    binding.constraintDetailContent.visibility = View.VISIBLE

                    adapter = DetailProductAdapter()
                    binding.vpDetailImage.adapter = adapter
                    adapter.submitList(it.data.image)

                    if (adapter.itemCount > 1) {
                        TabLayoutMediator(binding.tlDetail, binding.vpDetailImage) { tab, _ ->
                            tab.setCustomView(R.layout.custom_tab_indicator)
                        }.attach()
                    } else {
                        binding.tlDetail.visibility = View.GONE
                    }
                    it.data.productVariant?.forEach { variant ->
                        chip = Chip(requireActivity())
                        chip.setTextAppearanceResource(R.style.Theme_Ecommerce_ChipGroup_Chip)
                        chip.text = variant.variantName
                        chip.isCheckable = true
                        if (viewModel.selectedVariantName == "") {
                            chip.isChecked = true
                            viewModel.selectedVariantName = variant.variantName ?: ""
                            viewModel.selectedVariantPrice = (it.data.productPrice?.plus(
                                (variant.variantPrice ?: 0)
                            )) ?: 0
                            binding.tvDetailPrice.text = viewModel.selectedVariantPrice.toRupiah()

                        } else if (viewModel.selectedVariantName == variant.variantName) {
                            chip.isChecked = true
                            binding.tvDetailPrice.text = viewModel.selectedVariantPrice.toRupiah()
                        }
                        binding.chipgroupDetailVariant.addView(chip)
                    }

                    binding.chipgroupDetailVariant.setOnCheckedStateChangeListener { group, _ ->
                        it.data.productVariant?.forEach { variant ->
                            selectedChip = group.findViewById(group.checkedChipId)
                            if (selectedChip.text.toString() == variant.variantName) {
                                viewModel.selectedVariantName = variant.variantName ?: ""
                                viewModel.selectedVariantPrice = (it.data.productPrice?.plus(
                                    (variant.variantPrice ?: 0)
                                )) ?: 0
                                binding.tvDetailPrice.text =
                                    viewModel.selectedVariantPrice.toRupiah()

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

                    binding.tvDetailName.text = it.data.productName
                    binding.tvDetailSold.text =
                        "${getString(R.string.item_sold_title)} ${it.data.sale}"
                    binding.tvDetailRating.text =
                        "${it.data.productRating} (${it.data.totalRating})"
                    binding.tvDetailDescriptionContent.text = it.data.description
                    binding.tvDetailReviewRating.text = it.data.productRating.toString()
                    binding.tvDetailReviewSatisfaction.text =
                        "${it.data.totalSatisfaction}${getString(R.string.detail_satisfaction_desc)}"
                    binding.tvDetailReviewCount.text =
                        "${it.data.totalRating} ${getString(R.string.detail_rating_desc)} ${
                            getString(
                                R.string.detail_dot
                            )
                        } ${it.data.totalReview} ${getString(R.string.detail_review_desc)}"


                    analyticsViewItem(
                        bundle = bundleOf(
                            Param.ITEM_ID to it.data.productId,
                            Param.ITEM_NAME to it.data.productName,
                            Param.ITEM_BRAND to it.data.brand,
                        ),
                        value = it.data.productPrice!!.toDouble()
                    )
                }

                else -> {
                    binding.pbDetail.visibility = View.GONE
                    binding.constraintDetailContent.visibility = View.GONE
                    binding.scrollviewDetailError.visibility = View.VISIBLE
                }
            }
        }

        viewModel.getWishlistByProductId().distinctUntilChanged().observe(viewLifecycleOwner) {
            if (it != null) {
                wishlistState = true
                binding.ivDetailWishlist.setBackgroundResource(R.drawable.ic_favorite)
                if (wishlistPress) {
                    Snackbar.make(
                        requireView(),
                        "${getString(R.string.detail_success_add)} ${productDetail.productName} ${
                            getString(
                                R.string.detail_to_wishlist
                            )
                        }",
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                analyticsAddToWishlist(
                    bundle = bundleOf(
                        Param.ITEM_ID to productDetail.productId,
                        Param.ITEM_NAME to productDetail.productName,
                        Param.ITEM_BRAND to productDetail.brand,
                        Param.ITEM_VARIANT to viewModel.selectedVariantName
                    ),
                    value = viewModel.selectedVariantPrice.toDouble()
                )
            } else {
                wishlistState = false
                binding.ivDetailWishlist.setBackgroundResource(R.drawable.ic_favorite_border)
                if (wishlistPress) {
                    Snackbar.make(
                        requireView(),
                        "${getString(R.string.detail_success_remove)} ${productDetail.productName} ${
                            getString(
                                R.string.detail_from_wishlist
                            )
                        }",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
            wishlistPress = false
        }
    }

    private fun listener() {
        binding.btnDetailReviewShowAll.setOnClickListener {
            analytics.logEvent("btn_detail_review_show_all", null)
            bundle = bundleOf(REVIEW_KEY to productDetail.productId)
            findNavController().navigate(
                R.id.action_detailProductFragment_to_reviewFragment,
                bundle
            )
        }

        binding.toolbarDetail.setNavigationOnClickListener {
            analytics.logEvent("btn_detail_back", null)
            findNavController().navigateUp()
        }

        binding.ivDetailWishlist.setOnClickListener {
            wishlistPress = true
            if (wishlistState) {
                analytics.logEvent("btn_detail_wishlist_delete", null)
                viewModel.deleteWishlistById(productDetail.productId!!)
            } else {
                analytics.logEvent("btn_detail_wishlist_insert", null)
                viewModel.insertWishlist(
                    wishlistEntity = WishlistEntity(
                        id = productDetail.productId ?: System.currentTimeMillis().toString(),
                        image = productDetail.image?.get(0),
                        productName = productDetail.productName,
                        productPrice = productDetail.productPrice,
                        store = productDetail.store,
                        productRating = productDetail.productRating,
                        brand = productDetail.brand,
                        sale = productDetail.sale,
                        stock = productDetail.stock,
                        variantName = viewModel.selectedVariantName,
                        variantPrice = viewModel.selectedVariantPrice,
                    )
                )
            }
        }

        binding.ivDetailShare.setOnClickListener {
            analytics.logEvent("btn_detail_share", null)
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "${getString(R.string.all_name)} : ${productDetail.productName}\n" +
                            "${getString(R.string.all_price)} : ${productDetail.productPrice?.toRupiah()}\n" +
                            "${getString(R.string.all_link)} : $BASE_DEEPLINK${productDetail.productId}"
                )
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, "Ecommerce")
            startActivity(shareIntent)
        }

        binding.btnDetailCart.setOnClickListener {
            analytics.logEvent("btn_detail_cart", null)
            viewModel.addCart(
                cartEntity = CartEntity(
                    id = productDetail.productId ?: System.currentTimeMillis().toString(),
                    image = productDetail.image?.get(0),
                    productName = productDetail.productName,
                    productPrice = productDetail.productPrice,
                    brand = productDetail.brand,
                    store = productDetail.store,
                    stock = productDetail.stock,
                    variantPrice = viewModel.selectedVariantPrice,
                    variantName = viewModel.selectedVariantName
                )
            ).observe(viewLifecycleOwner) {
                when (it) {
                    is ViewState.Success -> {
                        when (it.data) {
                            "cart" -> {
                                Snackbar.make(
                                    requireView(),
                                    getString(R.string.all_success_add_cart),
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }

                            "quantity" -> {
                                Snackbar.make(
                                    requireView(),
                                    getString(R.string.all_success_update_quantity),
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }

                        analyticsAddToCart(
                            bundle = bundleOf(
                                Param.ITEM_ID to productDetail.productId,
                                Param.ITEM_NAME to productDetail.productName,
                                Param.ITEM_BRAND to productDetail.brand,
                                Param.ITEM_VARIANT to viewModel.selectedVariantName
                            ),
                            value = viewModel.selectedVariantPrice.toDouble()
                        )
                    }

                    is ViewState.Error -> {
                        Snackbar.make(
                            requireView(),
                            getString(R.string.all_stock_not_available),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    else -> {}
                }
            }
        }

        binding.layoutDetailError.btnErrorResetRefresh.setOnClickListener {
            analytics.logEvent("btn_detail_error_refresh", null)
            viewModel.getProductById()
        }

        binding.btnDetailBuy.setOnClickListener {
            analytics.logEvent("btn_detail_buy", null)

            analyticsBeginCheckout(
                bundleOf(
                    Param.ITEM_ID to productDetail.productId,
                    Param.ITEM_NAME to productDetail.productName,
                    Param.ITEM_BRAND to productDetail.brand,
                    Param.ITEM_VARIANT to viewModel.selectedVariantName
                ),
                viewModel.selectedVariantPrice.toDouble()
            )
            val bundle = bundleOf(
                CheckoutFragment.CHECKOUT_KEY to listOf(
                    CartEntity(
                        id = productDetail.productId!!,
                        productName = productDetail.productName,
                        image = productDetail.image?.get(0),
                        productPrice = productDetail.productPrice,
                        brand = productDetail.brand,
                        store = productDetail.store,
                        stock = productDetail.stock,
                        variantPrice = viewModel.selectedVariantPrice,
                        variantName = viewModel.selectedVariantName,
                        quantity = 1,
                    )
                )
            )
            (requireActivity() as MainActivity).toCheckOut(bundle)
        }
    }

    private fun analyticsViewItem(bundle: Bundle, value: Double) {
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM) {
            param(Param.ITEMS, bundle)
            param(Param.CURRENCY, "Rp")
            param(Param.VALUE, value)
        }
    }

    private fun analyticsAddToCart(bundle: Bundle, value: Double) {
        analytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART) {
            param(Param.ITEMS, bundle)
            param(Param.CURRENCY, "Rp")
            param(Param.VALUE, value)
        }
    }

    private fun analyticsAddToWishlist(
        bundle: Bundle,
        value: Double
    ) {
        analytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST) {
            param(Param.ITEMS, bundle)
            param(Param.CURRENCY, "Rp")
            param(Param.VALUE, value)
        }
    }

    private fun analyticsBeginCheckout(
        bundle: Bundle,
        value: Double
    ) {
        analytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT) {
            param(Param.ITEMS, bundle)
            param(Param.CURRENCY, "Rp")
            param(Param.VALUE, value)
        }
    }

    companion object {
        const val PRODUCT_ID_KEY = "productId"
    }
}