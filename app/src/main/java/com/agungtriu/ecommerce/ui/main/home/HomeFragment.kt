package com.agungtriu.ecommerce.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.agungtriu.ecommerce.databinding.FragmentHomeBinding
import com.agungtriu.ecommerce.helper.Language
import com.agungtriu.ecommerce.ui.base.BaseFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        listener()
    }

    private fun listener() {
        binding.btnHomeLogout.setOnClickListener {
            analytics.logEvent("btn_home_logout", null)
            viewModel.doLogout()
        }

        binding.switchHomeTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setTheme(isChecked)
        }

        binding.switchHomeLang.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.setLanguage(Language.ID.name)
            } else {
                viewModel.setLanguage(Language.EN.name)
            }
        }
    }

    private fun observeData() {
        viewModel.getThemeLang().observe(viewLifecycleOwner) {
            binding.switchHomeTheme.isChecked = it.isDark
            binding.switchHomeLang.isChecked = it.language == Language.ID.name
        }
    }
}
