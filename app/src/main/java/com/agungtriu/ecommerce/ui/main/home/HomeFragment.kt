package com.agungtriu.ecommerce.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.agungtriu.ecommerce.databinding.FragmentHomeBinding
import com.agungtriu.ecommerce.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnHomeLogout.setOnClickListener {
            viewModel.doLogout()
        }
    }
}