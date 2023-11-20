package com.agungtriu.ecommerce.ui.notification

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.agungtriu.ecommerce.databinding.FragmentNotificationBinding
import com.agungtriu.ecommerce.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment :
    BaseFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {

    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.selectNotifications()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        observeData()
        listener()
    }

    private fun setLayout() {
        adapter = NotificationAdapter(viewModel)
        binding.rvNotification.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotification.adapter = adapter
    }

    private fun observeData() {
        viewModel.resultNotifications.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.scrollviewNotificationError.isVisible = true
                binding.layoutNotificationError.btnErrorResetRefresh.isVisible = false
            } else {
                adapter.submitList(it)
            }
        }
    }

    private fun listener() {
        binding.toolbarNotification.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}