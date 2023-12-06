package com.agungtriu.ecommerce.ui.review.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.compose.theme.EcommerceAppComposeTheme
import com.agungtriu.ecommerce.compose.ui.ErrorScreen
import com.agungtriu.ecommerce.compose.ui.LoadingScreen
import com.agungtriu.ecommerce.compose.ui.TopBarScreen
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.review.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewComposeFragment : Fragment() {
    private val viewModel: ReviewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                EcommerceAppComposeTheme {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Scaffold(
                            topBar = {
                                TopBarScreen(
                                    findNavController(),
                                    stringResource(id = R.string.all_review_buyer)
                                )
                            },
                            content = { paddingValues ->
                                Column(
                                    modifier = Modifier
                                        .padding(paddingValues)
                                        .fillMaxHeight()
                                        .background(MaterialTheme.colorScheme.surface),
                                ) {
                                    Divider(
                                        thickness = 1.dp,
                                        color = MaterialTheme.colorScheme.outlineVariant
                                    )
                                    viewModel.resultReview.observeAsState().value.let {
                                        when (it) {
                                            is ViewState.Loading -> Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                LoadingScreen()
                                            }

                                            is ViewState.Success -> ReviewsContent(it.data)
                                            is ViewState.Error ->
                                                ErrorScreen(error = it.error) {
                                                    viewModel.getReviewsByProductId()
                                                }

                                            else -> {}
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
