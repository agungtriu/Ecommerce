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
import com.agungtriu.ecommerce.compose.theme.theme
import com.agungtriu.ecommerce.compose.ui.ErrorScreen
import com.agungtriu.ecommerce.compose.ui.LoadingScreen
import com.agungtriu.ecommerce.compose.ui.TopBarScreen
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.review.ReviewViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewComposeFragment : Fragment() {
    private val viewModel: ReviewViewModel by viewModels()
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                theme {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Scaffold(
                            topBar = {
                                TopBarScreen(
                                    findNavController = findNavController(),
                                    title = stringResource(id = R.string.all_review_buyer),
                                    analytics = analytics,
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

                                            is ViewState.Success -> {
                                                ReviewsContent(reviews = it.data)
                                            }

                                            is ViewState.Error ->
                                                ErrorScreen(
                                                    responseError = it.error,
                                                    context = context,
                                                    analytics = analytics
                                                ) {
                                                    viewModel.getReviewsByProductId()
                                                }

                                            else -> {
                                                viewModel.getReviewsByProductId()
                                            }
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
