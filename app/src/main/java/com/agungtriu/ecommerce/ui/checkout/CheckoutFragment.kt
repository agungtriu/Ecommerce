package com.agungtriu.ecommerce.ui.checkout

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.remote.model.request.ProductFulfillment
import com.agungtriu.ecommerce.core.remote.model.request.RequestFulfillment
import com.agungtriu.ecommerce.core.remote.model.response.DataPayment
import com.agungtriu.ecommerce.databinding.FragmentCheckoutBinding
import com.agungtriu.ecommerce.helper.Extension.toRupiah
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.base.BaseFragment
import com.agungtriu.ecommerce.ui.status.StatusFragment.Companion.STATUS_KEY
import com.agungtriu.ecommerce.ui.status.StatusModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Param
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutFragment : BaseFragment<FragmentCheckoutBinding>(FragmentCheckoutBinding::inflate) {
    private val viewModel: CheckoutViewModel by viewModels()
    private lateinit var adapter: CheckoutAdapter
    private lateinit var analytics: FirebaseAnalytics
    private val listProduct = mutableListOf<ProductFulfillment>()
    private var totalPay = 0
    private lateinit var bundles: Array<Bundle>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        observeData()
        listener()
    }

    private fun setLayout() {
        adapter = CheckoutAdapter(viewModel)
        binding.rvCheckout.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCheckout.adapter = adapter
    }

    private fun observeData() {
        viewModel.listProduct.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            totalPay = 0
            listProduct.clear()
            bundles = arrayOf(bundleOf())
            it.forEach { item ->
                totalPay += item.variantPrice?.times(item.quantity!!) ?: 0
                listProduct.add(
                    ProductFulfillment(
                        productId = item.id,
                        variantName = item.variantName ?: "",
                        quantity = item.quantity ?: 1
                    )
                )

                bundles += bundleOf(
                    Param.ITEM_ID to item.id,
                    Param.ITEM_NAME to item.productName,
                    Param.ITEM_BRAND to item.brand,
                    Param.ITEM_VARIANT to item.variantName,
                )
            }
            binding.tvCheckoutTotalPay.text = totalPay.toRupiah()
        }

        val currentBackStackEntry = findNavController().currentBackStackEntry
        val savedStateHandle = currentBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<DataPayment>(RESULT_FROM_PAYMENT)
            ?.observe(currentBackStackEntry, Observer { result ->
                viewModel.setDataPayment(result)
            })

        viewModel.dataPayment.observe(viewLifecycleOwner) {
            if (it?.label != null) {
                Glide.with(requireContext())
                    .load(it.image)
                    .into(binding.ivCheckoutPayment)
                binding.tvCheckoutPayment.text = it.label
                binding.btnCheckoutBuy.isEnabled = true

                analyticsAddPaymentInfo(
                    bundles = bundles,
                    value = totalPay.toDouble(),
                    payment = it.label!!
                )
            }
        }
    }

    private fun listener() {
        binding.toolbarCheckout.setNavigationOnClickListener {
            analytics.logEvent("btn_checkout_back", null)
            findNavController().navigateUp()
        }

        binding.cvCheckoutPayment.setOnClickListener {
            analytics.logEvent("btn_checkout_payment", null)
            findNavController().navigate(R.id.action_checkoutFragment_to_choosePaymentFragment)
        }

        binding.btnCheckoutBuy.setOnClickListener {
            analytics.logEvent("btn_checkout_buy", null)
            viewModel.postFulfillment(
                requestFulfillment = RequestFulfillment(
                    payment = binding.tvCheckoutPayment.text.toString(),
                    items = listProduct
                )
            ).observe(viewLifecycleOwner) {
                when (it) {
                    is ViewState.Loading -> {
                        binding.pbCheckout.isVisible = true
                        binding.btnCheckoutBuy.isVisible = false
                    }

                    is ViewState.Error -> {
                        binding.pbCheckout.isVisible = false
                        binding.btnCheckoutBuy.isVisible = true
                        Snackbar.make(
                            requireView(),
                            it.error.message.toString(),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    is ViewState.Success -> {
                        binding.pbCheckout.isVisible = false
                        binding.btnCheckoutBuy.isVisible = true

                        val bundle = bundleOf(
                            STATUS_KEY to StatusModel(
                                date = it.data.date,
                                total = it.data.total,
                                invoiceId = it.data.invoiceId,
                                payment = it.data.payment,
                                time = it.data.time,
                                status = it.data.status
                            )
                        )
                        analyticsPurchase(
                            date = it.data.date!!,
                            bundles = bundles,
                            transactionId = it.data.invoiceId!!,
                            value = it.data.total!!.toDouble()
                        )
                        findNavController().navigate(
                            R.id.action_checkoutFragment_to_statusFragment,
                            bundle
                        )
                    }
                }
            }
        }
    }

    private fun analyticsPurchase(
        date: String,
        bundles: Array<Bundle>,
        transactionId: String,
        value: Double
    ) {
        analytics.logEvent(FirebaseAnalytics.Event.PURCHASE) {
            param(Param.CURRENCY, "Rp")
            param(Param.END_DATE, date)
            param(Param.ITEMS, bundles)
            param(Param.START_DATE, date)
            param(Param.TRANSACTION_ID, transactionId)
            param(Param.VALUE, value)
        }
    }

    private fun analyticsAddPaymentInfo(
        bundles: Array<Bundle>,
        value: Double,
        payment: String
    ) {
        analytics.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO) {
            param(Param.ITEMS, bundles)
            param(Param.CURRENCY, "Rp")
            param(Param.VALUE, value)
            param(Param.PAYMENT_TYPE, payment)

        }
    }

    companion object {
        const val CHECKOUT_KEY = "CheckoutKey"
        const val RESULT_FROM_PAYMENT = "ResultFromPayment"
    }
}