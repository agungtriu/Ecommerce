package com.agungtriu.ecommerce.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.databinding.FragmentMainBinding
import com.agungtriu.ecommerce.ui.base.BaseFragment
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils.attachBadgeDrawable
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var badgeWishlist: BadgeDrawable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fcv_main_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        binding.bnvMainFragment.setupWithNavController(navController)

        badgeWishlist = binding.bnvMainFragment.getOrCreateBadge(R.id.wishlistFragment)

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
            badgeWishlist.isVisible = it.isNotEmpty()
            badgeWishlist.number = it.size
        }
        viewModel.getQuantity().observe(viewLifecycleOwner) {
            val badgeCart = BadgeDrawable.create(requireContext())

            badgeCart.isVisible = it != 0
            badgeCart.number = it ?: 0
            if (it != null) {
                attachBadgeDrawable(badgeCart, binding.toolbarMain, R.id.btn_main_shopping_cart)
            } else {
                badgeCart.clearNumber()
            }
        }
    }


    private fun listener() {
        binding.toolbarMain.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_main_notification -> {
                    Snackbar.make(requireView(), "coming soon", Snackbar.LENGTH_LONG).show()
                    true
                }

                R.id.btn_main_shopping_cart -> {
                    findNavController().navigate(R.id.action_mainFragment_to_cartFragment)
                    true
                }

                R.id.btn_main_menu -> {
                    Snackbar.make(requireView(), "coming soon", Snackbar.LENGTH_LONG).show()
                    true
                }

                else -> false
            }
        }
    }
}