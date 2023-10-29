package com.agungtriu.ecommerce.ui.prelogin.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.remote.model.response.DataLogin
import com.agungtriu.ecommerce.databinding.FragmentLoginBinding
import com.agungtriu.ecommerce.helper.Extension.makeLinks
import com.agungtriu.ecommerce.helper.FormValidation.isEmailValid
import com.agungtriu.ecommerce.helper.FormValidation.isPasswordValid
import com.agungtriu.ecommerce.helper.Utils.closeSoftKeyboard
import com.agungtriu.ecommerce.helper.Utils.getColorAttribute
import com.agungtriu.ecommerce.helper.ViewState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var isEmailValid = false
    private var isPasswordValid = false

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkOnBoarding()
        checkLoginStatus()
    }

    private fun checkOnBoarding() {
        if (!viewModel.getOnBoardingStatus()) {
            findNavController().navigate(R.id.action_loginFragment_to_onBoardingFragment)
        }
    }

    private fun checkLoginStatus() {
        val dataLogin = viewModel.getLoginStatus()
        if (dataLogin.isLogin) {
            if (dataLogin.userName != "") {
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            } else {
                findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        listener()
    }

    private fun observeData() {
        viewModel.resultLogin.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                    binding.pbLogin.visibility = View.VISIBLE
                }

                is ViewState.Success<DataLogin> -> {
                    binding.pbLogin.visibility = View.GONE
                    if (it.data.userName != "") {
                        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                    } else {
                        findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
                    }
                }

                is ViewState.Error -> {
                    binding.pbLogin.visibility = View.GONE
                    Snackbar.make(requireView(), it.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun listener() {
        binding.tvLoginAgreement.makeLinks(Pair(getString(R.string.all_terms_conditions),
            View.OnClickListener {
                Snackbar.make(it.rootView, "Coming soon ...", Snackbar.LENGTH_LONG).show()
            }), Pair(getString(R.string.all_privacy_policy), View.OnClickListener {
            Snackbar.make(it.rootView, "Coming soon ...", Snackbar.LENGTH_LONG).show()
        })
        )

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
                binding.tvLoginStateEmail.text = getString(R.string.all_email_example)
                isEmailValid = true
                binding.tilLoginEmail.boxStrokeColor = getColorAttribute(
                    requireContext(), com.google.android.material.R.attr.colorPrimary
                )
                binding.tvLoginStateEmail.setTextColor(
                    getColorAttribute(
                        requireContext(), com.google.android.material.R.attr.colorOnSurfaceVariant
                    )
                )
            } else {
                binding.tvLoginStateEmail.text = getString(R.string.all_email_not_valid)
                isEmailValid = false
                binding.tilLoginEmail.boxStrokeColor = getColorAttribute(
                    requireContext(), com.google.android.material.R.attr.colorError
                )
                binding.tvLoginStateEmail.setTextColor(
                    getColorAttribute(
                        requireContext(), com.google.android.material.R.attr.colorError
                    )
                )
            }
            buttonValidation(emailState = isEmailValid, passwordState = isPasswordValid)
        }
        binding.tietLoginPassword.addTextChangedListener {
            if (isPasswordValid(it.toString())) {
                isPasswordValid = true
                binding.tvLoginStatePassword.text = getString(R.string.all_password_state)
                binding.tilLoginPassword.boxStrokeColor = getColorAttribute(
                    requireContext(), com.google.android.material.R.attr.colorPrimary
                )
                binding.tvLoginStatePassword.setTextColor(
                    getColorAttribute(
                        requireContext(), com.google.android.material.R.attr.colorOnSurfaceVariant
                    )
                )
            } else {
                isPasswordValid = false
                binding.tvLoginStatePassword.text = getString(R.string.all_password_not_valid)
                binding.tilLoginPassword.boxStrokeColor = getColorAttribute(
                    requireContext(), com.google.android.material.R.attr.colorError
                )
                binding.tvLoginStatePassword.setTextColor(
                    getColorAttribute(
                        requireContext(), com.google.android.material.R.attr.colorError
                    )
                )
            }
            buttonValidation(emailState = isEmailValid, passwordState = isPasswordValid)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun buttonValidation(emailState: Boolean, passwordState: Boolean) {
        binding.btnLogin.isEnabled = emailState && passwordState
    }
}