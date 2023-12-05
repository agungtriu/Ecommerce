package com.agungtriu.ecommerce.ui.cart

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.databinding.FragmentCartBinding
import com.agungtriu.ecommerce.helper.Extension.toRupiah
import com.agungtriu.ecommerce.ui.AppActivity
import com.agungtriu.ecommerce.ui.base.BaseFragment
import com.agungtriu.ecommerce.ui.checkout.CheckoutFragment.Companion.CHECKOUT_KEY
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Param
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>(FragmentCartBinding::inflate) {
    private val viewModel: CartViewModel by viewModels()
    private lateinit var adapter: CartAdapter
    private lateinit var analytics: FirebaseAnalytics
    private var isChecked = false
    private var isVisibleDelete = false
    private lateinit var listProduct: List<CartEntity>
    private var totalPay = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCarts()
        analytics = Firebase.analytics
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        observeData()
        listener()
    }

    private fun setLayout() {
        adapter = CartAdapter(viewModel, requireActivity(), analytics)
        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCart.adapter = adapter
    }

    private fun observeData() {
        viewModel.resultCarts.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                listProduct = it
                adapter.submitList(it)
                binding.scrollviewCartError.isVisible = false
                binding.constraintCartContent.isVisible = true

                totalPay = 0
                isVisibleDelete = false
                isChecked = true
                it.forEach { item ->
                    if (item.isSelected == true) {
                        isVisibleDelete = true
                        totalPay += item.variantPrice?.times(item.quantity!!) ?: 0
                    } else {
                        isChecked = false
                    }
                }

                binding.btnCartBuy.isEnabled = isVisibleDelete
                binding.btnCartDelete.isVisible = isVisibleDelete
                binding.cbCartSelectAll.isChecked = isChecked
                binding.tvCartTotalPay.text = (totalPay).toRupiah()

                var bundles = arrayOf(bundleOf())

                it.forEach { product ->
                    bundles += bundleOf(
                        Param.ITEM_ID to product.id,
                        Param.ITEM_NAME to product.productName,
                        Param.ITEM_BRAND to product.brand,
                        Param.ITEM_VARIANT to product.variantName
                    )
                }

                analyticsViewCart(bundles, totalPay.toDouble())
            } else {
                binding.scrollviewCartError.isVisible = true
                binding.layoutCartError.btnErrorResetRefresh.isVisible = false
                binding.constraintCartContent.isVisible = false
            }
        }
    }

    private fun listener() {
        binding.toolbarCart.setNavigationOnClickListener {
            analytics.logEvent("btn_cart_back", null)
            findNavController().navigateUp()
        }
        binding.cbCartSelectAll.setOnClickListener {
            viewModel.updateCartsIsSelected(!isChecked)
        }

        binding.btnCartDelete.setOnClickListener {
            analytics.logEvent("btn_cart_delete_selected", null)
            viewModel.deleteCartsSelected()
        }

        binding.btnCartBuy.setOnClickListener {
            analytics.logEvent("btn_cart_buy", null)
            val selectedProducts = listProduct.filter { it.isSelected == true }

            var bundles = arrayOf(bundleOf())
            selectedProducts.forEach {
                bundles += bundleOf(
                    Param.ITEM_ID to it.id,
                    Param.ITEM_NAME to it.productName,
                    Param.ITEM_BRAND to it.brand,
                    Param.ITEM_VARIANT to it.variantName

                )
            }
            analyticsBeginCheckout(bundles, totalPay.toDouble())

            val bundle = bundleOf(CHECKOUT_KEY to selectedProducts)
            (requireActivity() as AppActivity).navigate(R.id.action_global_to_checkout_fragment, bundle)
        }
    }

    private fun analyticsViewCart(bundle: Array<Bundle>, value: Double) {
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_CART) {
            param(Param.ITEMS, bundle)
            param(Param.CURRENCY, "Rp")
            param(Param.VALUE, value)
        }
    }

    private fun analyticsBeginCheckout(
        bundles: Array<Bundle>,
        value: Double
    ) {
        analytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT) {
            param(Param.ITEMS, bundles)
            param(Param.CURRENCY, "Rp")
            param(Param.VALUE, value)
        }
    }
}
