package com.agungtriu.ecommerce.ui.payment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.agungtriu.ecommerce.databinding.FragmentChoosePaymentBinding
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChoosePaymentFragment :
    BaseFragment<FragmentChoosePaymentBinding>(FragmentChoosePaymentBinding::inflate) {
    private val viewModel: PaymentViewModel by viewModels()
    private lateinit var adapter: PaymentParentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        observeData()
        listener()
    }

    private fun setLayout() {
        adapter = PaymentParentAdapter(findNavController())
        binding.rvChoosePayment.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChoosePayment.adapter = adapter
    }

    private fun observeData() {
        viewModel.getFirebasePayment().observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                    binding.pbChoosePayment.isVisible = true
                }

                is ViewState.Error -> {
                    binding.pbChoosePayment.isVisible = false
                    binding.scrollviewChoosePaymentError.isVisible = true
                }

                is ViewState.Success -> {
                    binding.pbChoosePayment.isVisible = false
                    adapter.submitList(it.data)
                }
            }
        }
        viewModel.updateFirebasePayment().observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                    binding.pbChoosePayment.isVisible = true
                }

                is ViewState.Error -> {
                    binding.pbChoosePayment.isVisible = false
                    binding.scrollviewChoosePaymentError.isVisible = true
                }

                is ViewState.Success -> {
                    binding.pbChoosePayment.isVisible = false
                    adapter.submitList(it.data)
                }
            }
        }
    }

    private fun listener() {
        binding.toolbarChoosePayment.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.layoutChoosePaymentError.btnErrorResetRefresh.setOnClickListener {
            viewModel.getFirebasePayment()
        }
    }
}