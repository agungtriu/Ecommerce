package com.agungtriu.ecommerce.ui.prelogin.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.databinding.FragmentOnBoardingBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {
    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: OnboardingAdapter
    private val listOfImage = listOf(
        R.mipmap.ic_onboarding_one,
        R.mipmap.ic_onboarding_two,
        R.mipmap.ic_onboarding_three
    )

    private val viewModel: OnBoardingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = OnboardingAdapter(listOfImage)
        binding.vpOnboarding.adapter = adapter
        TabLayoutMediator(binding.tlOnboarding, binding.vpOnboarding) { tab, _ ->
            tab.setCustomView(R.layout.custom_tab_indicator)
        }.attach()

        listener()
    }

    private fun listener() {
        binding.btnOnboardingNext.setOnClickListener {
            binding.vpOnboarding.currentItem = binding.vpOnboarding.currentItem + 1
        }
        binding.btnOnboardingJoin.setOnClickListener {
            viewModel.saveOnBoardingStatus()
            findNavController().navigate(R.id.action_onBoardingFragment_to_registerFragment)
        }
        binding.btnOnboardingSkip.setOnClickListener {
            viewModel.saveOnBoardingStatus()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}