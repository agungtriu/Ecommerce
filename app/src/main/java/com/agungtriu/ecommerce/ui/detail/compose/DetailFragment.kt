package com.agungtriu.ecommerce.ui.detail.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.compose.theme.theme
import com.agungtriu.ecommerce.compose.ui.ErrorScreen
import com.agungtriu.ecommerce.compose.ui.LoadingScreen
import com.agungtriu.ecommerce.compose.ui.TopBarScreen
import com.agungtriu.ecommerce.core.remote.model.response.DataDetailProduct
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.detail.DetailProductViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private val viewModel: DetailProductViewModel by viewModels()
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
                        val snackBarHostState = remember { SnackbarHostState() }
                        Scaffold(
                            snackbarHost = {
                                SnackbarHost(hostState = snackBarHostState)
                            },
                            topBar = {
                                TopBarScreen(
                                    findNavController = findNavController(),
                                    title = stringResource(R.string.detail_product),
                                    analytics = analytics
                                )
                            }
                        ) { paddingValues ->
                            Column(
                                modifier = Modifier
                                    .padding(paddingValues)
                                    .background(MaterialTheme.colorScheme.surface),
                            ) {
                                Divider(
                                    thickness = 1.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                                viewModel.resultDetail.observeAsState().value.let {
                                    when (it) {
                                        is ViewState.Loading -> Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) { LoadingScreen() }

                                        is ViewState.Success -> {
                                            analyticsViewItem(it.data)
                                            DetailContentScreen(
                                                activity = requireActivity(),
                                                context = context,
                                                data = it.data,
                                                viewModel = viewModel,
                                                findNavController = findNavController(),
                                                snackBarHostState = snackBarHostState,
                                                analytics = analytics
                                            )
                                        }

                                        is ViewState.Error -> ErrorScreen(
                                            responseError = it.error,
                                            context = context,
                                            analytics = analytics
                                        ) { viewModel.getProductById() }

                                        else -> {}
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun analyticsViewItem(item: DataDetailProduct) {
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM) {
            param(
                FirebaseAnalytics.Param.ITEMS,
                bundleOf(
                    FirebaseAnalytics.Param.ITEM_ID to item.productId,
                    FirebaseAnalytics.Param.ITEM_NAME to item.productName,
                    FirebaseAnalytics.Param.ITEM_BRAND to item.brand,
                )
            )
            param(FirebaseAnalytics.Param.CURRENCY, "Rp")
            param(FirebaseAnalytics.Param.VALUE, item.productPrice!!.toDouble())
        }
    }
}
