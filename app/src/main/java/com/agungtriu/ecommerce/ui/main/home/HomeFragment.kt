package com.agungtriu.ecommerce.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.agungtriu.ecommerce.databinding.FragmentHomeBinding
import com.agungtriu.ecommerce.helper.Language
import com.agungtriu.ecommerce.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        listener()
    }

    private fun listener() {
        binding.btnHomeLogout.setOnClickListener {
            viewModel.doLogout()
        }

        binding.switchHomeTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.changeTheme(isChecked)
        }

        binding.switchHomeLang.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.changeLanguage(Language.id.name)
            } else {
                viewModel.changeLanguage(Language.en.name)
            }
        }
    }

    private fun observeData() {
        viewModel.getThemeLang().observe(viewLifecycleOwner) {
            binding.switchHomeTheme.isChecked = it.isDark
            binding.switchHomeLang.isChecked = it.language == Language.id.name
        }
    }
}