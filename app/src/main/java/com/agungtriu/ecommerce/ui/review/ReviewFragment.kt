package com.agungtriu.ecommerce.ui.review

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.agungtriu.ecommerce.databinding.FragmentReviewBinding
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.base.BaseFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : BaseFragment<FragmentReviewBinding>(FragmentReviewBinding::inflate) {
    private val viewModel: ReviewViewModel by viewModels()
    private lateinit var adapter: ReviewAdapter
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        listener()
    }

    private fun observeData() {
        viewModel.resultReview.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                    binding.pbReview.visibility = View.VISIBLE
                    binding.scrollviewReviewError.visibility = View.GONE
                    binding.rvReview.visibility = View.GONE
                }

                is ViewState.Error -> {
                    binding.pbReview.visibility = View.GONE
                    binding.scrollviewReviewError.visibility = View.VISIBLE
                    binding.layoutReviewError.btnErrorResetRefresh.isVisible = false
                    binding.rvReview.visibility = View.GONE
                }

                is ViewState.Success -> {
                    binding.pbReview.visibility = View.GONE
                    binding.scrollviewReviewError.visibility = View.GONE
                    binding.rvReview.visibility = View.VISIBLE

                    adapter = ReviewAdapter()
                    binding.rvReview.adapter = adapter
                    binding.rvReview.layoutManager = LinearLayoutManager(requireContext())
                    adapter.submitList(it.data)
                }
            }
        }
    }

    private fun listener() {
        binding.toolbarReview.setNavigationOnClickListener {
            analytics.logEvent("btn_review_back", null)
            findNavController().navigateUp()
        }
    }

    companion object {
        const val REVIEW_KEY = "reviewKey"
    }
}