package com.agungtriu.ecommerce.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
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
import com.agungtriu.ecommerce.ui.base.BaseFragment
import com.agungtriu.ecommerce.ui.status.StatusFragment.Companion.STATE_STATUS_KEY
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils.attachBadgeDrawable
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var badgeWishlist: BadgeDrawable
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var screenWidth: WindowWidthSizeClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fcv_main_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        screenWidth = computeWindowSizeClasses()

        // Select the appropriate layout
        when (screenWidth) {
            COMPACT -> {
                binding.bnvMainFragment?.setupWithNavController(navController)
                badgeWishlist = binding.bnvMainFragment!!.getOrCreateBadge(R.id.wishlistFragment)
            }

            MEDIUM -> {
                binding.nrvMainFragment?.setupWithNavController(navController)
                badgeWishlist = binding.nrvMainFragment!!.getOrCreateBadge(R.id.wishlistFragment)
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
            if (it.isLogin) {
                if (it.userName != "" && it.userName != null) {
                    binding.toolbarMain.title = it.userName
                } else {
                    findNavController().navigate(R.id.action_mainFragment_to_profileFragment)
                }
            }
        }

        viewModel.getWishlists().observe(viewLifecycleOwner) {
            if (it != null && (screenWidth == COMPACT || screenWidth == MEDIUM)) {
                badgeWishlist.isVisible = it.isNotEmpty()
                badgeWishlist.number = it.size
            }
        }
        viewModel.selectCountCart().observe(viewLifecycleOwner) {
            val badgeCart = BadgeDrawable.create(requireContext())

            badgeCart.isVisible = it != 0
            badgeCart.number = it ?: 0
            if (it != null) {
                attachBadgeDrawable(badgeCart, binding.toolbarMain, R.id.btn_main_shopping_cart)
            } else {
                badgeCart.clearNumber()
            }
        }

        viewModel.selectCountNotification().observe(viewLifecycleOwner) {
            val badgeCart = BadgeDrawable.create(requireContext())
            badgeCart.isVisible = it != 0
            badgeCart.number = it ?: 0
            if (it != null) {
                attachBadgeDrawable(badgeCart, binding.toolbarMain, R.id.btn_main_notification)
            } else {
                badgeCart.clearNumber()
            }
        }

        if (arguments?.getString(STATE_STATUS_KEY) == "transaction") {
            when (screenWidth) {
                COMPACT -> {
                    binding.bnvMainFragment?.selectedItemId = R.id.transactionFragment
                }

                MEDIUM -> {
                    binding.nrvMainFragment?.selectedItemId = R.id.transactionFragment
                }

                EXPANDED -> {
                    binding.nvMainFragment?.menu?.getItem(3)?.isChecked = true
                }
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
                    if (screenWidth == EXPANDED) {
                        binding.dlMainFragment?.open()
                    }
                    true
                }

                else -> false
            }
        }
        if (screenWidth == EXPANDED) {
            binding.nvMainFragment?.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.homeFragment -> {
                        binding.fcvMainFragment.findNavController()
                            .navigate(R.id.action_global_to_home_fragment)
                    }

                    R.id.storeFragment -> {
                        binding.fcvMainFragment.findNavController()
                            .navigate(R.id.action_global_to_store_fragment)
                    }

                    R.id.wishlistFragment -> {
                        binding.fcvMainFragment.findNavController()
                            .navigate(R.id.action_global_to_wishlist_fragment)
                    }

                    R.id.transactionFragment -> {
                        binding.fcvMainFragment.findNavController()
                            .navigate(R.id.action_global_to_transaction_fragment)
                    }
                }
                menuItem.isChecked = true
                binding.dlMainFragment?.close()
                true
            }
        }
    }

    private fun computeWindowSizeClasses(): WindowWidthSizeClass {
        val metrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(requireActivity())
        val width = metrics.bounds.width()
        val height = metrics.bounds.height()
        val density = resources.displayMetrics.density
        val windowSizeClass = WindowSizeClass.compute(width / density, height / density)
        // COMPACT, MEDIUM, or EXPANDED
        return windowSizeClass.windowWidthSizeClass

    }
}
