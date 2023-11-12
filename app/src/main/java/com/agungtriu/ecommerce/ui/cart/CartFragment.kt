package com.agungtriu.ecommerce.ui.cart

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.agungtriu.ecommerce.databinding.FragmentCartBinding
import com.agungtriu.ecommerce.helper.Extension.toRupiah
import com.agungtriu.ecommerce.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>(FragmentCartBinding::inflate) {
    private val viewModel: CartViewModel by viewModels()
    private lateinit var adapter: CartAdapter
    private var isChecked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCarts()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        observeData()
        listener()
    }

    private fun setLayout() {
        adapter = CartAdapter(viewModel, requireActivity())
        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCart.adapter = adapter
    }

    private fun observeData() {
        viewModel.getTotalPay().observe(viewLifecycleOwner) {
            binding.tvCartTotalPay.text = (it ?: 0).toRupiah()
        }
        viewModel.resultCarts.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.constraintCartError.isVisible = false
                binding.constraintCartContent.isVisible = true
                adapter.submitList(it)
            } else {
                binding.constraintCartError.isVisible = true
                binding.constraintCartContent.isVisible = false
            }
        }
        viewModel.checkCartIsSelected(true).observe(viewLifecycleOwner) {
            binding.btnCartDelete.isVisible = it.isNotEmpty()
        }
        viewModel.checkCartIsSelected(false).observe(viewLifecycleOwner) {
            isChecked = it.isNullOrEmpty()
            binding.cbCartSelectAll.isChecked = isChecked
        }
    }

    private fun listener() {
        binding.toolbarCart.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.cbCartSelectAll.setOnClickListener {
            viewModel.updateAllCartIsSelected(!isChecked)
        }

        binding.btnCartDelete.setOnClickListener {
            viewModel.deleteSelected()
        }
    }
}