package com.agungtriu.ecommerce.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fcv_main_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.bnvMainFragment.setupWithNavController(navController)

        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModel.getLoginStatus().observe(viewLifecycleOwner) {
            if (it.isLogin) {
                if (it.userName != "") {
                    binding.tvHomeUsername.text = it.userName
                } else {
                    findNavController().navigate(R.id.action_mainFragment_to_profileFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}