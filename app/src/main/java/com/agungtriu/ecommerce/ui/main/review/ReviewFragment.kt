package com.agungtriu.ecommerce.ui.main.review

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.agungtriu.ecommerce.databinding.FragmentReviewBinding
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : BaseFragment<FragmentReviewBinding>(FragmentReviewBinding::inflate) {
    private val viewModel: ReviewViewModel by viewModels()
    private lateinit var adapter: ReviewAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        binding.toolbarReview.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeData() {
        viewModel.resultReview.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                    binding.pbReview.visibility = View.VISIBLE
                    binding.constraintReviewError.visibility = View.GONE
                    binding.rvReview.visibility = View.GONE
                }

                is ViewState.Error -> {
                    binding.pbReview.visibility = View.GONE
                    binding.constraintReviewError.visibility = View.VISIBLE
                    binding.rvReview.visibility = View.GONE
                }

                is ViewState.Success -> {
                    binding.pbReview.visibility = View.GONE
                    binding.constraintReviewError.visibility = View.GONE
                    binding.rvReview.visibility = View.VISIBLE

                    adapter = ReviewAdapter()
                    binding.rvReview.adapter = adapter
                    binding.rvReview.layoutManager = LinearLayoutManager(requireContext())
                    adapter.submitList(it.data)
                }
            }
        }
    }

    companion object {
        const val REVIEW_KEY = "reviewKey"
    }
}