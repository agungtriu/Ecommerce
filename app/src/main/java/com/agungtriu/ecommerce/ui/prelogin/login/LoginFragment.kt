package com.agungtriu.ecommerce.ui.prelogin.login

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.databinding.FragmentLoginBinding
import com.agungtriu.ecommerce.helper.Extension.setColor
import com.agungtriu.ecommerce.helper.FormValidation.isEmailValid
import com.agungtriu.ecommerce.helper.FormValidation.isPasswordValid
import com.agungtriu.ecommerce.helper.Utils.closeSoftKeyboard
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.base.BaseFragment
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private var isEmailValid = false
    private var isPasswordValid = false

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkOnBoarding()
    }

    private fun checkOnBoarding() {
        if (!viewModel.getOnBoardingStatus()) {
            findNavController().navigate(R.id.action_loginFragment_to_onBoardingFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        listener()

        binding.tvLoginAgreement.setColor(
            getString(R.string.all_terms_conditions),
            MaterialColors.getColor(requireView(), com.google.android.material.R.attr.colorPrimary)
        )

        binding.tvLoginAgreement.setColor(
            getString(R.string.all_privacy_policy),
            MaterialColors.getColor(requireView(), com.google.android.material.R.attr.colorPrimary)
        )
    }

    private fun observeData() {
        viewModel.resultLogin.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                    binding.pbLogin.visibility = View.VISIBLE
                    binding.btnLogin.visibility = View.INVISIBLE
                }

                is ViewState.Success -> {
                    binding.pbLogin.visibility = View.GONE
                    binding.btnLogin.visibility = View.VISIBLE
                }

                is ViewState.Error -> {
                    binding.pbLogin.visibility = View.GONE
                    binding.btnLogin.visibility = View.VISIBLE
                    Snackbar.make(requireView(), it.error.message ?: "", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun listener() {
        binding.btnLogin.setOnClickListener {
            closeSoftKeyboard(binding.tietLoginPassword, requireContext())
            viewModel.doLogin(
                email = binding.tietLoginEmail.text.toString(),
                password = binding.tietLoginPassword.text.toString()
            )
        }

        binding.btnLoginRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.tietLoginEmail.addTextChangedListener {
            if (isEmailValid(it.toString())) {
                isEmailValid = true
                binding.tilLoginEmail.error = null
            } else {
                isEmailValid = false
                binding.tilLoginEmail.error = getString(R.string.all_email_not_valid)
            }

            if (it.isNullOrBlank()) {
                binding.tilLoginEmail.error = null
            }
            buttonValidation(emailState = isEmailValid, passwordState = isPasswordValid)
        }
        binding.tietLoginPassword.addTextChangedListener {
            if (isPasswordValid(it.toString())) {
                isPasswordValid = true
                binding.tilLoginPassword.error = null
            } else {
                isPasswordValid = false
                binding.tilLoginPassword.error = getString(R.string.all_password_not_valid)
            }

            if (it.isNullOrBlank()) {
                binding.tilLoginPassword.error = null
            }
            buttonValidation(emailState = isEmailValid, passwordState = isPasswordValid)
        }
    }

    private fun buttonValidation(emailState: Boolean, passwordState: Boolean) {
        binding.btnLogin.isEnabled = emailState && passwordState
    }
}