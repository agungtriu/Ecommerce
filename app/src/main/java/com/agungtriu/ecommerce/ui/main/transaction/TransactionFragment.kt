package com.agungtriu.ecommerce.ui.main.transaction

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.agungtriu.ecommerce.databinding.FragmentTransactionBinding
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.base.BaseFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionFragment :
    BaseFragment<FragmentTransactionBinding>(FragmentTransactionBinding::inflate) {

    private val viewModel: TransactionViewModel by viewModels()
    private lateinit var adapter: TransactionAdapter
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayout()
        listener()
        observerData()
    }

    private fun setLayout() {
        adapter = TransactionAdapter(requireActivity())
        binding.rvTransaction.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransaction.adapter = adapter
    }

    private fun observerData() {
        viewModel.resultTransaction.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                    binding.pbTransaction.isVisible = true
                    binding.scrollviewTransactionError.isVisible = false
                }

                is ViewState.Error -> {
                    binding.pbTransaction.isVisible = false
                    binding.scrollviewTransactionError.isVisible = true
                }

                is ViewState.Success -> {
                    binding.pbTransaction.isVisible = false
                    adapter.submitList(it.data)
                }
            }
        }
    }

    private fun listener() {
        binding.layoutTransactionError.btnErrorResetRefresh.setOnClickListener {
            analytics.logEvent("btn_transaction_error_refresh", null)
            viewModel.getTransactions()
        }
    }
}
