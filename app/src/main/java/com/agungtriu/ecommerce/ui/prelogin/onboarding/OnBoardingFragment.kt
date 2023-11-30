package com.agungtriu.ecommerce.ui.prelogin.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.databinding.FragmentOnBoardingBinding
import com.agungtriu.ecommerce.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment :
    BaseFragment<FragmentOnBoardingBinding>(FragmentOnBoardingBinding::inflate) {

    private lateinit var adapter: OnboardingAdapter
    private val listOfImage = listOf(
        R.mipmap.ic_onboarding_one,
        R.mipmap.ic_onboarding_two,
        R.mipmap.ic_onboarding_three
    )

    private val viewModel: OnBoardingViewModel by viewModels()
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        listener()
    }

    private fun setLayout() {
        adapter = OnboardingAdapter()
        binding.vpOnboarding.adapter = adapter
        adapter.submitList(listOfImage)
        TabLayoutMediator(binding.tlOnboarding, binding.vpOnboarding) { tab, _ ->
            tab.setCustomView(R.layout.custom_tab_indicator)
        }.attach()
    }

    private fun listener() {
        binding.btnOnboardingNext.setOnClickListener {
            analytics.logEvent("btn_onboarding_next", null)
            binding.vpOnboarding.currentItem = binding.vpOnboarding.currentItem + 1
        }
        binding.btnOnboardingJoin.setOnClickListener {
            analytics.logEvent("btn_onboarding_join", null)
            viewModel.setOnBoardingStatus()
            findNavController().navigate(R.id.action_onBoardingFragment_to_registerFragment)
        }
        binding.btnOnboardingSkip.setOnClickListener {
            analytics.logEvent("btn_onboarding_skip", null)
            viewModel.setOnBoardingStatus()
            findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
        }

        binding.vpOnboarding.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == listOfImage.size - 1) {
                    binding.btnOnboardingNext.visibility = View.INVISIBLE
                } else {
                    binding.btnOnboardingNext.visibility = View.VISIBLE
                }
            }
        })
    }
}
