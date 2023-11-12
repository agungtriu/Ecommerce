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
import com.agungtriu.ecommerce.ui.base.BaseFragment
import com.agungtriu.ecommerce.ui.main.review.ReviewFragment.Companion.REVIEW_KEY
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailProductFragment :
    BaseFragment<FragmentDetailProductBinding>(FragmentDetailProductBinding::inflate) {

    private val viewModel: DetailProductViewModel by viewModels()
    private lateinit var adapter: DetailProductAdapter
    private lateinit var productDetail: DataDetailProduct
    private lateinit var chip: Chip
    private lateinit var bundle: Bundle
    private var wishlistState = false
    private var wishlistPress = false
    private var selectedVariantName = ""
    private var selectedVariantPrice = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        listener()
    }

    @SuppressLint("SetTextI18n")
    private fun observeData() {
        viewModel.getDetailProduct().observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                    binding.pbDetail.visibility = View.VISIBLE
                    binding.constraintDetailContent.visibility = View.GONE
                    binding.constraintDetailError.visibility = View.GONE
                }

                is ViewState.Error -> {
                    binding.pbDetail.visibility = View.GONE
                    binding.constraintDetailContent.visibility = View.GONE
                    binding.constraintDetailError.visibility = View.VISIBLE
                }

                is ViewState.Success -> {
                    productDetail = it.data
                    binding.pbDetail.visibility = View.GONE
                    binding.constraintDetailError.visibility = View.GONE
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
                    var checked = false
                    it.data.productVariant?.forEach { variant ->
                        chip = Chip(requireActivity())
                        chip.text = variant.variantName
                        chip.isCheckable = true
                        if (!checked) {
                            chip.isChecked = true
                            checked = true
                            selectedVariantName = variant.variantName ?: ""
                            selectedVariantPrice = (it.data.productPrice?.plus(
                                (variant.variantPrice ?: 0)
                            )) ?: 0
                            binding.tvDetailPrice.text = selectedVariantPrice.toRupiah()

                        }
                        binding.chipgroupDetailVariant.addView(chip)
                    }

                    binding.chipgroupDetailVariant.setOnCheckedStateChangeListener { group, _ ->
                        it.data.productVariant?.forEach { variant ->
                            val selectedChip = group.findViewById<Chip>(group.checkedChipId)
                            if (selectedChip.text.toString() == variant.variantName) {
                                selectedVariantName = variant.variantName ?: ""
                                selectedVariantPrice = (it.data.productPrice?.plus(
                                    (variant.variantPrice ?: 0)
                                )) ?: 0
                                binding.tvDetailPrice.text = selectedVariantPrice.toRupiah()
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
                }
            }
        }

        viewModel.getWishlist().distinctUntilChanged().observe(viewLifecycleOwner) {
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
            bundle = bundleOf(REVIEW_KEY to productDetail.productId)
            findNavController().navigate(
                R.id.action_detailProductFragment_to_reviewFragment,
                bundle
            )
        }

        binding.toolbarDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.ivDetailWishlist.setOnClickListener {
            wishlistPress = true
            if (wishlistState) {
                viewModel.deleteWishlistById(productDetail.productId!!)
            } else {
                viewModel.insertWishlist(
                    wishlistEntity = WishlistEntity(
                        id = productDetail.productId ?: System.currentTimeMillis().toString(),
                        image = productDetail.image?.get(0),
                        productName = productDetail.productName,
                        productPrice = productDetail.productPrice,
                        store = productDetail.store,
                        productRating = productDetail.productRating,
                        sale = productDetail.sale,
                        stock = productDetail.stock,
                        variantName = selectedVariantName,
                        variantPrice = selectedVariantPrice,
                    )
                )
            }
        }

        binding.ivDetailShare.setOnClickListener {
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
            viewModel.addCart(
                cartEntity = CartEntity(
                    id = productDetail.productId ?: System.currentTimeMillis().toString(),
                    image = productDetail.image?.get(0),
                    productName = productDetail.productName,
                    productPrice = productDetail.productPrice,
                    store = productDetail.store,
                    stock = productDetail.stock,
                    variantPrice = selectedVariantPrice,
                    variantName = selectedVariantName
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

        binding.btnDetailErrorRefresh.setOnClickListener {
            viewModel.getDetailProduct()
        }
    }

    companion object {
        const val PRODUCT_ID_KEY = "productId"
    }
}