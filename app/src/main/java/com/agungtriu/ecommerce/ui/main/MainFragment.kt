package com.agungtriu.ecommerce.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.databinding.FragmentMainBinding
import com.agungtriu.ecommerce.ui.MainActivity
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
    private var screenWidth: Int = 0
    private lateinit var wishlist: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fcv_main_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        screenWidth = getScreenWidth()

        // Select the appropriate layout
        if (screenWidth < 600) {
            binding.bnvMainFragment?.setupWithNavController(navController)
            badgeWishlist = binding.bnvMainFragment!!.getOrCreateBadge(R.id.wishlistFragment)
        } else if (screenWidth in 600..839) {
            binding.nrvMainFragment?.setupWithNavController(navController)
            badgeWishlist = binding.nrvMainFragment!!.getOrCreateBadge(R.id.wishlistFragment)
        } else if (screenWidth > 840) {
            binding.nvMainFragment?.setupWithNavController(navController)
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
            if (it != null && screenWidth < 840) {
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
            if (screenWidth < 600) {
                binding.bnvMainFragment?.selectedItemId = R.id.transactionFragment
            } else if (screenWidth in 600..839) {
                binding.nrvMainFragment?.selectedItemId = R.id.transactionFragment
            } else if (screenWidth > 840) {
                binding.nvMainFragment?.menu?.getItem(3)?.isChecked = true
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
                    if (screenWidth > 840) {
                        binding.dlMainFragment?.open()
                    }
                    true
                }

                else -> false
            }
        }

        binding.nvMainFragment?.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    binding.fcvMainFragment.findNavController().navigate(R.id.action_global_to_home_fragment)
                }

                R.id.storeFragment -> {
                    binding.fcvMainFragment.findNavController().navigate(R.id.action_global_to_store_fragment)
                }

                R.id.wishlistFragment -> {
                    binding.fcvMainFragment.findNavController().navigate(R.id.action_global_to_wishlist_fragment)
                }

                R.id.transactionFragment -> {
                    binding.fcvMainFragment.findNavController().navigate(R.id.action_global_to_transaction_fragment)
                }
            }
            menuItem.isChecked = true
            binding.dlMainFragment?.close()
            true
        }
    }

    private fun getScreenWidth(): Int {
        val displayMetrics = resources.displayMetrics
        return displayMetrics.widthPixels
    }
}
