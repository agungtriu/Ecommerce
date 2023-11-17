package com.agungtriu.ecommerce.ui.prelogin.register

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.databinding.FragmentRegisterBinding
import com.agungtriu.ecommerce.helper.Extension.setColor
import com.agungtriu.ecommerce.helper.FormValidation.isEmailValid
import com.agungtriu.ecommerce.helper.FormValidation.isPasswordValid
import com.agungtriu.ecommerce.helper.Utils.closeSoftKeyboard
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.MainActivity
import com.agungtriu.ecommerce.ui.base.BaseFragment
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {
    private var isEmailValid = false
    private var isPasswordValid = false
    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        observeData()
        listener()
    }

    private fun setLayout() {
        binding.tvRegisterAgreement.setColor(
            getString(R.string.all_terms_conditions),
            MaterialColors.getColor(requireView(), com.google.android.material.R.attr.colorPrimary)
        )

        binding.tvRegisterAgreement.setColor(
            getString(R.string.all_privacy_policy),
            MaterialColors.getColor(requireView(), com.google.android.material.R.attr.colorPrimary)
        )
    }

    private fun listener() {
        binding.btnRegisterLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.tietRegisterEmail.addTextChangedListener {
            if (isEmailValid(it.toString())) {
                isEmailValid = true
                binding.tilRegisterEmail.error = null
            } else {
                isEmailValid = false
                binding.tilRegisterEmail.error = getString(R.string.all_email_not_valid)
            }
            if (it.isNullOrBlank()) {
                binding.tilRegisterEmail.error = null
            }
            buttonValidation(isEmailValid, isPasswordValid)
        }

        binding.tietRegisterPassword.addTextChangedListener {
            if (isPasswordValid(it.toString())) {
                isPasswordValid = true
                binding.tilRegisterPassword.error = null
            } else {
                isPasswordValid = false
                binding.tilRegisterPassword.error = getString(R.string.all_password_not_valid)
            }
            if (it.isNullOrBlank()) {
                binding.tilRegisterPassword.error = null
            }
            buttonValidation(isEmailValid, isPasswordValid)
        }

        binding.btnRegister.setOnClickListener {
            closeSoftKeyboard(binding.tietRegisterPassword, requireContext())
            viewModel.doRegister(
                email = binding.tietRegisterEmail.text.toString(),
                password = binding.tietRegisterPassword.text.toString()
            )
        }
    }

    private fun observeData() {
        viewModel.resultRegister.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                    binding.pbRegister.visibility = View.VISIBLE
                    binding.btnRegister.visibility = View.INVISIBLE
                }

                is ViewState.Success -> {
                    binding.pbRegister.visibility = View.GONE
                    binding.btnRegister.visibility = View.VISIBLE
                    (requireActivity() as MainActivity).toMain()
                }

                is ViewState.Error -> {
                    binding.pbRegister.visibility = View.GONE
                    binding.btnRegister.visibility = View.VISIBLE
                    Snackbar.make(requireView(), it.error.message ?: "", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun buttonValidation(emailState: Boolean, passwordState: Boolean) {
        binding.btnRegister.isEnabled = emailState && passwordState
    }
}