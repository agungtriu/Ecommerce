package com.agungtriu.ecommerce.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.window.core.layout.WindowWidthSizeClass.Companion.COMPACT
import androidx.window.core.layout.WindowWidthSizeClass.Companion.EXPANDED
import androidx.window.core.layout.WindowWidthSizeClass.Companion.MEDIUM
import androidx.window.layout.WindowMetricsCalculator
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.databinding.FragmentMainBinding
import com.agungtriu.ecommerce.helper.Screen
import com.agungtriu.ecommerce.ui.AppActivity
import com.agungtriu.ecommerce.ui.base.BaseFragment
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils.attachBadgeDrawable
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    private val viewModel: MainViewModel by viewModels()
    private var badgeWishlist: BadgeDrawable? = null
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var screenWidth: WindowWidthSizeClass

    private val navHostFragment by lazy {
        childFragmentManager.findFragmentById(R.id.fcv_main_fragment) as NavHostFragment
    }

    private val navController by lazy {
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!viewModel.getLoginStatus()) {
            (requireActivity() as AppActivity).navigate(R.id.action_global_to_prelogin_navigation)
        }
        analytics = Firebase.analytics
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        computeWindowSizeClasses()

        when (screenWidth) {
            COMPACT -> {
                binding.bnvMainFragment?.setupWithNavController(navController)
                badgeWishlist = binding.bnvMainFragment?.getOrCreateBadge(R.id.wishlistFragment)
            }

            MEDIUM -> {
                binding.nrvMainFragment?.setupWithNavController(navController)
                badgeWishlist = binding.nrvMainFragment?.getOrCreateBadge(R.id.wishlistFragment)
            }

            EXPANDED -> {
                binding.nvMainFragment?.setupWithNavController(navController)
            }
        }

        observeData()
        listener()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun observeData() {
        viewModel.getLoginData().observe(viewLifecycleOwner) {
            if (it.isLogin == true) {
                if (it.userName != "" && it.userName != null) {
                    binding.toolbarMain.title = it.userName
                } else {
                    findNavController().navigate(R.id.action_mainFragment_to_profileFragment)
                }
            }
        }
        viewModel.getWishlists().observe(viewLifecycleOwner) {
            badgeWishlist?.isVisible = !it.isNullOrEmpty()
            badgeWishlist?.number = it?.size ?: 0
        }
        val badgeCart = BadgeDrawable.create(requireContext())
        attachBadgeDrawable(badgeCart, binding.toolbarMain, R.id.btn_main_shopping_cart)
        viewModel.selectCountCart().observe(viewLifecycleOwner) {
            badgeCart.isVisible = it != 0
            badgeCart.number = it
        }

        val badgeNotification = BadgeDrawable.create(requireContext())
        attachBadgeDrawable(badgeNotification, binding.toolbarMain, R.id.btn_main_notification)
        viewModel.selectCountNotification().observe(viewLifecycleOwner) {
            badgeNotification.isVisible = it != 0
            badgeNotification.number = it
        }

        if (viewModel.stateTransaction == Screen.TRANSACTION.name) {
            viewModel.setStateTransaction(null)
            when (screenWidth) {
                COMPACT -> binding.bnvMainFragment?.selectedItemId = R.id.transactionFragment
                MEDIUM -> binding.nrvMainFragment?.selectedItemId = R.id.transactionFragment
                EXPANDED -> binding.nvMainFragment?.menu?.performIdentifierAction(
                    R.id.transactionFragment,
                    0
                )
            }
        }
    }

    private fun listener() {
        binding.toolbarMain.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_main_notification -> {
                    analytics.logEvent("btn_main_notification", null)
                    findNavController().navigate(R.id.action_mainFragment_to_notificationFragment)
                    true
                }

                R.id.btn_main_shopping_cart -> {
                    analytics.logEvent("btn_main_cart", null)
                    findNavController().navigate(R.id.action_mainFragment_to_cartFragment)
                    true
                }

                R.id.btn_main_menu -> {
                    analytics.logEvent("btn_main_menu", null)
                    true
                }

                else -> false
            }
        }
    }

    private fun computeWindowSizeClasses() {
        val metrics =
            WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(requireActivity())
        val width = metrics.bounds.width()
        val height = metrics.bounds.height()
        val density = resources.displayMetrics.density
        val windowSizeClass = WindowSizeClass.compute(width / density, height / density)
        // COMPACT, MEDIUM, or EXPANDED
        screenWidth = windowSizeClass.windowWidthSizeClass
    }
}
