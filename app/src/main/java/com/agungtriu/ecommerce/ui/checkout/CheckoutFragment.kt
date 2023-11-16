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
import com.agungtriu.ecommerce.core.remote.model.request.Product
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutFragment : BaseFragment<FragmentCheckoutBinding>(FragmentCheckoutBinding::inflate) {
    private val viewModel: CheckoutViewModel by viewModels()
    private lateinit var adapter: CheckoutAdapter
    private val listProduct = mutableListOf<Product>()
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
            var totalPay = 0
            listProduct.clear()
            it.forEach { item ->
                totalPay += item.variantPrice?.times(item.quantity!!) ?: 0
                listProduct.add(
                    Product(
                        productId = item.id,
                        variantName = item.variantName ?: "",
                        quantity = item.quantity ?: 1
                    )
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
            if (it != null) {
                Glide.with(requireContext())
                    .load(it.image)
                    .into(binding.ivCheckoutPayment)
                binding.tvCheckoutPayment.text = it.label
                binding.btnCheckoutBuy.isEnabled = true
            }
        }
    }

    private fun listener() {
        binding.toolbarCheckout.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.cvCheckoutPayment.setOnClickListener {
            findNavController().navigate(R.id.action_checkoutFragment_to_choosePaymentFragment)
        }

        binding.btnCheckoutBuy.setOnClickListener {
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
                        findNavController().navigate(
                            R.id.action_checkoutFragment_to_statusFragment,
                            bundle
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val CHECKOUT_KEY = "CheckoutKey"
        const val RESULT_FROM_PAYMENT = "ResultFromPayment"
    }
}