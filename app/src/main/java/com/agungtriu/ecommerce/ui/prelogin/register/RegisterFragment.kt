package com.agungtriu.ecommerce.ui.prelogin.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.remote.model.response.DataRegister
import com.agungtriu.ecommerce.databinding.FragmentRegisterBinding
import com.agungtriu.ecommerce.helper.Extension.makeLinks
import com.agungtriu.ecommerce.helper.FormValidation.isEmailValid
import com.agungtriu.ecommerce.helper.FormValidation.isPasswordValid
import com.agungtriu.ecommerce.helper.Utils
import com.agungtriu.ecommerce.helper.Utils.closeSoftKeyboard
import com.agungtriu.ecommerce.helper.ViewState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private var isEmailValid = false
    private var isPasswordValid = false
    private val viewModel: RegisterViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        listener()
    }

    private fun listener() {
        binding.tvRegisterAgreement.makeLinks(Pair(
            getString(R.string.all_terms_conditions),
            View.OnClickListener {
                Snackbar.make(it.rootView, "Coming soon ...", Snackbar.LENGTH_LONG).show()
            }), Pair(getString(R.string.all_privacy_policy), View.OnClickListener {
            Snackbar.make(it.rootView, "Coming soon ...", Snackbar.LENGTH_LONG).show()
        })
        )

        binding.btnRegisterLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.tietRegisterEmail.addTextChangedListener {
            if (isEmailValid(it.toString())) {
                isEmailValid = true
                binding.tvRegisterStateEmail.text = getString(R.string.all_email_example)
                binding.tilRegisterEmail.boxStrokeColor = Utils.getColorAttribute(
                    requireContext(), com.google.android.material.R.attr.colorPrimary
                )
                binding.tvRegisterStateEmail.setTextColor(
                    Utils.getColorAttribute(
                        requireContext(), com.google.android.material.R.attr.colorOnSurfaceVariant
                    )
                )
            } else {
                isEmailValid = false
                binding.tvRegisterStateEmail.text = getString(R.string.all_email_not_valid)
                binding.tilRegisterEmail.boxStrokeColor = Utils.getColorAttribute(
                    requireContext(), com.google.android.material.R.attr.colorError
                )
                binding.tvRegisterStateEmail.setTextColor(
                    Utils.getColorAttribute(
                        requireContext(), com.google.android.material.R.attr.colorError
                    )
                )
            }

            buttonValidation(isEmailValid, isPasswordValid)
        }
        binding.tietRegisterPassword.addTextChangedListener {
            if (isPasswordValid(it.toString())) {
                isPasswordValid = true
                binding.tvRegisterStatePassword.text = getString(R.string.all_password_state)
                binding.tilRegisterPassword.boxStrokeColor = Utils.getColorAttribute(
                    requireContext(), com.google.android.material.R.attr.colorPrimary
                )
                binding.tvRegisterStatePassword.setTextColor(
                    Utils.getColorAttribute(
                        requireContext(), com.google.android.material.R.attr.colorOnSurfaceVariant
                    )
                )
            } else {
                isPasswordValid = false
                binding.tvRegisterStatePassword.text = getString(R.string.all_password_not_valid)
                binding.tilRegisterPassword.boxStrokeColor = Utils.getColorAttribute(
                    requireContext(), com.google.android.material.R.attr.colorError
                )
                binding.tvRegisterStatePassword.setTextColor(
                    Utils.getColorAttribute(
                        requireContext(), com.google.android.material.R.attr.colorError
                    )
                )
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
                }

                is ViewState.Success<DataRegister> -> {
                    binding.pbRegister.visibility = View.GONE
                }

                is ViewState.Error -> {
                    binding.pbRegister.visibility = View.GONE
                    Snackbar.make(requireView(), it.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun buttonValidation(emailState: Boolean, passwordState: Boolean) {
        binding.btnRegister.isEnabled = emailState && passwordState
    }
}