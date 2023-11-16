package com.agungtriu.ecommerce.ui.notification

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.agungtriu.ecommerce.databinding.FragmentNotificationBinding
import com.agungtriu.ecommerce.ui.base.BaseFragment

class NotificationFragment :
    BaseFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.scrollviewNotificationError.isVisible = true
        binding.layoutNotificationError.btnErrorResetRefresh.isVisible = false
        listener()
    }

    private fun listener() {
        binding.toolbarNotification.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}