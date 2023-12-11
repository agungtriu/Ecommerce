package com.agungtriu.ecommerce.ui.status

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.remote.model.request.RequestRating
import com.agungtriu.ecommerce.databinding.FragmentStatusBinding
import com.agungtriu.ecommerce.helper.Extension.toRupiah
import com.agungtriu.ecommerce.helper.Screen
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.AppActivity
import com.agungtriu.ecommerce.ui.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatusFragment : BaseFragment<FragmentStatusBinding>(FragmentStatusBinding::inflate) {

    private val viewModel: StatusViewModel by viewModels()
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayout()
        setupBackPresser()
        listener()
    }

    private fun setLayout() {
        binding.tvStatusId.text = viewModel.dataFulfillment.invoiceId
        binding.tvStatusStatus.text =
            if (viewModel.dataFulfillment.status == true) {
                getString(R.string.all_success)
            } else {
                getString(
                    R.string.all_fail
                )
            }
        binding.tvStatusDate.text = viewModel.dataFulfillment.date
        binding.tvStatusTime.text = viewModel.dataFulfillment.time
        binding.tvStatusPayment.text = viewModel.dataFulfillment.payment
        binding.tvStatusTotalPay.text = viewModel.dataFulfillment.total?.toRupiah()
        binding.rbStatus.rating = (viewModel.dataFulfillment.rating ?: 0).toFloat()
        binding.tietStatusReview.setText(viewModel.dataFulfillment.review)
    }

    private fun listener() {
        binding.btnStatusDone.setOnClickListener {
            analytics.logEvent("btn_status_done", null)
            val bundle = bundleOf(
                STATE_STATUS_KEY to viewModel.stateStatus
            )
            if (binding.rbStatus.rating.toInt() == 0 && binding.tietStatusReview.text.toString() == "") {
                if (viewModel.stateStatus == Screen.TRANSACTION.name) {
                    findNavController().navigateUp()
                } else {
                    (requireActivity() as AppActivity).navigate(R.id.action_global_to_main_navigation)
                }
            } else {
                viewModel.postRating(
                    requestRating = RequestRating(
                        invoiceId = viewModel.dataFulfillment.invoiceId
                            ?: System.currentTimeMillis()
                                .toString(),
                        rating = if (binding.rbStatus.rating.toInt() > 0) {
                            binding.rbStatus.rating.toInt()
                        } else {
                            null
                        },
                        review = if (binding.tietStatusReview.text.toString() != "") {
                            binding.tietStatusReview.text.toString()
                        } else {
                            null
                        }
                    )
                ).observe(viewLifecycleOwner) {
                    when (it) {
                        is ViewState.Loading -> {
                            binding.btnStatusDone.visibility = View.INVISIBLE
                            binding.pbStatus.isVisible = true
                        }

                        is ViewState.Error -> {
                            binding.btnStatusDone.visibility = View.VISIBLE
                            binding.pbStatus.isVisible = false
                            Snackbar.make(
                                requireView(),
                                it.error.message.toString(),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }

                        is ViewState.Success -> {
                            binding.btnStatusDone.visibility = View.VISIBLE
                            binding.pbStatus.isVisible = false
                            (requireActivity() as AppActivity).navigate(
                                R.id.action_global_to_main_navigation,
                                bundle
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun setupBackPresser() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            }
        )
    }

    companion object {
        const val STATUS_KEY = "StatusKey"
        const val STATE_STATUS_KEY = "StateStatusKey"
    }
}
