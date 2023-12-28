package com.agungtriu.ecommerce.ui.detail.compose

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.remote.model.response.DataDetailProduct
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.core.room.entity.WishlistEntity
import com.agungtriu.ecommerce.helper.Config
import com.agungtriu.ecommerce.helper.Extension.toRupiah
import com.agungtriu.ecommerce.helper.Screen
import com.agungtriu.ecommerce.helper.Utils.displayPrice
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.AppActivity
import com.agungtriu.ecommerce.ui.checkout.CheckoutFragment
import com.agungtriu.ecommerce.ui.detail.DetailProductViewModel
import com.agungtriu.ecommerce.ui.review.ReviewFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun DetailContentScreen(
    activity: FragmentActivity,
    context: Context,
    data: DataDetailProduct,
    viewModel: DetailProductViewModel,
    findNavController: NavController,
    snackBarHostState: SnackbarHostState,
    analytics: FirebaseAnalytics
) {
    val scope = rememberCoroutineScope()
    var selectedVariant by rememberSaveable { mutableIntStateOf(0) }
    var isWishlist by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(true) {
        val wishlist = viewModel.getWishlistCompose()
        val cart = viewModel.getCartCompose()
        isWishlist = wishlist != null

        data.productVariant?.forEachIndexed { index, item ->
            when (viewModel.stateDetail) {
                Screen.WISHLIST.name -> {
                    if (wishlist?.variantName == item.variantName) {
                        selectedVariant = index
                    }
                }

                Screen.CART.name -> {
                    if (cart?.variantName == item.variantName) {
                        selectedVariant = index
                    }
                }
            }
        }
    }

    val pagerState = rememberPagerState(pageCount = {
        data.image?.size ?: 0
    })

    Column {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(weight = 1f, fill = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                HorizontalPager(state = pagerState) {
                    AsyncImage(
                        model = data.image?.get(it),
                        placeholder = painterResource(id = R.mipmap.ic_thumbnail),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                }
                if ((data.image?.size ?: 0) > 1) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(data.image?.size ?: 0) { iteration ->
                            val color = if (pagerState.currentPage == iteration) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.outlineVariant
                            }
                            Box(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .background(color, CircleShape)
                                    .size(8.dp)
                            )
                        }
                    }
                }
            }

            Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp)) {
                Text(
                    text = displayPrice(
                        basePrice = data.productPrice,
                        variantPrice = data.productVariant?.get(selectedVariant)?.variantPrice
                    ).toRupiah(),
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_600)),
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false,
                        )
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .weight(1F)
                )
                IconButton(
                    onClick = {
                        analytics.logEvent("btn_detail_share", null)
                        intentShare(context = context, data = data)
                    },
                    modifier = Modifier
                        .defaultMinSize(
                            minWidth = 1.dp,
                            minHeight = 1.dp
                        )
                        .size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                IconButton(
                    onClick = {
                        if (isWishlist) {
                            analytics.logEvent("btn_detail_wishlist_delete", null)
                            viewModel.deleteWishlistById(data.productId!!)
                        } else {
                            analytics.logEvent("btn_detail_wishlist_insert", null)
                            analyticsEvent(
                                analytics = analytics,
                                firebaseEvent = FirebaseAnalytics.Event.ADD_TO_WISHLIST,
                                item = data,
                                selectedVariant = selectedVariant
                            )
                            viewModel.insertWishlist(data.toWishlist(selectedVariant))
                        }

                        viewModel.getWishlistCompose().let {
                            isWishlist = it != null
                            scope.launch {
                                snackBarHostState.showSnackbar(
                                    if (it != null) {
                                        getString(context, R.string.detail_success_add)
                                            .plus(" ${data.productName}")
                                            .plus(
                                                " ${
                                                    getString(
                                                        context,
                                                        R.string.detail_to_wishlist
                                                    )
                                                }"
                                            )
                                    } else {
                                        getString(context, R.string.detail_success_remove)
                                            .plus(" ${data.productName}")
                                            .plus(
                                                " ${
                                                    getString(
                                                        context,
                                                        R.string.detail_from_wishlist
                                                    )
                                                }"
                                            )
                                    }
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .defaultMinSize(
                            minWidth = 1.dp,
                            minHeight = 1.dp
                        )
                        .size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isWishlist) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                        ),
                        contentDescription = ""
                    )
                }
            }
            Text(
                text = data.productName ?: "",
                fontFamily = FontFamily(Font(R.font.poppins_400)),
                style = MaterialTheme.typography.bodyMedium.plus(
                    TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false,
                        )
                    )
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp)

            )
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 10.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.item_sold_title).plus(" ${data.sale}"),
                    fontFamily = FontFamily(Font(R.font.poppins_400)),
                    style = MaterialTheme.typography.bodySmall.plus(
                        TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false,
                            )
                        )
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .border(
                            width = 1.dp,
                            shape = MaterialTheme.shapes.extraSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        .padding(4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = "",
                        modifier = Modifier
                            .size(width = 15.dp, height = 15.dp)
                    )
                    Text(
                        text = data.productRating.toString().plus(" (${data.totalRating})"),
                        fontFamily = FontFamily(Font(R.font.poppins_400)),
                        style = MaterialTheme.typography.bodySmall.plus(
                            TextStyle(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false,
                                )
                            )
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 4.dp),
                    )
                }
                Spacer(modifier = Modifier.weight(1F))
            }
            Divider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier
                    .padding(vertical = 12.dp)
            )
            Text(
                text = stringResource(id = R.string.detail_pick_variant),
                fontFamily = FontFamily(Font(R.font.poppins_500)),
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false,
                    )
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            FlowRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                data.productVariant?.forEachIndexed { index, productVariant ->
                    InputChip(
                        onClick = {
                            selectedVariant = index

                            analytics.logEvent("btn_detail_variant", null)
                            analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
                                param(
                                    FirebaseAnalytics.Param.ITEMS,
                                    bundleOf(
                                        FirebaseAnalytics.Param.ITEM_NAME to data.productVariant?.get(
                                            index
                                        )?.variantName
                                    )
                                )
                                param(FirebaseAnalytics.Param.ITEM_LIST_NAME, "Variant")
                            }
                        },
                        label = {
                            Text(
                                text = productVariant.variantName ?: "",
                                style = MaterialTheme.typography.labelLarge.plus(
                                    TextStyle(
                                        platformStyle = PlatformTextStyle(
                                            includeFontPadding = false
                                        )
                                    )
                                ),
                                fontFamily = FontFamily(Font(R.font.poppins_500)),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        selected = selectedVariant == index
                    )
                }
            }
            Divider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier
                    .padding(vertical = 12.dp)
            )

            Text(
                text = stringResource(id = R.string.detail_description_product),
                fontFamily = FontFamily(Font(R.font.poppins_500)),
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false,
                    )
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Text(
                text = data.description ?: "",
                fontFamily = FontFamily(Font(R.font.poppins_400)),
                style = MaterialTheme.typography.bodyMedium.plus(
                    TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false,
                        )
                    )
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Divider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier
                    .padding(vertical = 12.dp)
            )

            Row {
                Text(
                    text = stringResource(id = R.string.all_review_buyer),
                    fontFamily = FontFamily(Font(R.font.poppins_500)),
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false,
                        )
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1F)
                )
                TextButton(
                    onClick = {
                        analytics.logEvent("btn_detail_review_show_all", null)
                        findNavController.navigate(
                            R.id.action_detailFragment_to_reviewComposeFragment,
                            bundleOf(
                                ReviewFragment.REVIEW_KEY to data.productId
                            )
                        )
                    },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(height = 22.dp, width = 100.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.detail_show_all_review),
                        fontFamily = FontFamily(Font(R.font.poppins_500)),
                        style = MaterialTheme.typography.labelMedium.plus(
                            TextStyle(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false,
                                )
                            )
                        ),
                    )
                }
            }
            Row(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 18.dp,
                    top = 8.dp
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = "",
                )
                Text(
                    text = data.productRating.toString(),
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_600)),
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false,
                        )
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .alignByBaseline()
                )
                Text(
                    text = stringResource(id = R.string.detail_rating_scala),
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_400)),
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false,
                        )
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.alignByBaseline()
                )
                Column(modifier = Modifier.padding(start = 32.dp)) {
                    Text(
                        text = data.totalSatisfaction.toString()
                            .plus(stringResource(id = R.string.detail_satisfaction_desc)),
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_600)),
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false,
                            )
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "${data.totalRating} ${stringResource(id = R.string.detail_rating_desc)}"
                            .plus(" ${stringResource(id = R.string.detail_dot)} ")
                            .plus("${data.totalReview} ${stringResource(id = R.string.detail_review_desc)}"),
                        style = MaterialTheme.typography.bodySmall.plus(
                            TextStyle(
                                platformStyle = PlatformTextStyle(includeFontPadding = false)
                            )
                        ),
                        fontFamily = FontFamily(Font(R.font.poppins_400)),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        Divider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = {
                    analytics.logEvent("btn_detail_buy", null)
                    analyticsEvent(
                        analytics = analytics,
                        firebaseEvent = FirebaseAnalytics.Event.BEGIN_CHECKOUT,
                        item = data,
                        selectedVariant = selectedVariant
                    )
                    (activity as AppActivity).navigate(
                        R.id.action_global_to_checkout_fragment,
                        bundle = bundleOf(
                            CheckoutFragment.CHECKOUT_KEY to listOf(
                                data.toCart(
                                    selectedVariant
                                )
                            )
                        )
                    )
                },
                modifier = Modifier
                    .weight(1F)
            ) {
                Text(
                    text = stringResource(id = R.string.detail_buy_now),
                    fontFamily = FontFamily(Font(R.font.poppins_500)),
                    style = MaterialTheme.typography.labelLarge.plus(
                        TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Button(
                onClick = {
                    analytics.logEvent("btn_detail_cart", null)
                    viewModel.addCartCompose(data.toCart(selectedVariant)).let {
                        when (it) {
                            is ViewState.Error -> {
                                scope.launch {
                                    snackBarHostState.showSnackbar(
                                        getString(context, R.string.all_stock_not_available)
                                    )
                                }
                            }

                            is ViewState.Success -> {
                                when (it.data) {
                                    "cart" -> scope.launch {
                                        snackBarHostState.showSnackbar(
                                            getString(context, R.string.all_success_add_cart)
                                        )
                                    }

                                    "quantity" -> scope.launch {
                                        snackBarHostState.showSnackbar(
                                            getString(
                                                context,
                                                R.string.all_success_update_quantity
                                            )
                                        )
                                    }
                                }
                                analyticsEvent(
                                    analytics = analytics,
                                    firebaseEvent = FirebaseAnalytics.Event.ADD_TO_CART,
                                    item = data,
                                    selectedVariant = selectedVariant
                                )
                            }

                            is ViewState.Loading -> {
                            }
                        }
                    }
                },
                modifier = Modifier
                    .weight(1F),
            ) {
                Text(
                    text = stringResource(id = R.string.detail_add_cart),
                    fontFamily = FontFamily(Font(R.font.poppins_500)),
                    style = MaterialTheme.typography.labelLarge.plus(
                        TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                )
            }
        }
    }
}

fun intentShare(context: Context, data: DataDetailProduct) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            """
                ${getString(context, R.string.all_name)} : ${data.productName}
                ${getString(context, R.string.all_price)} : ${data.productPrice?.toRupiah()}
                ${getString(context, R.string.all_link)} : ${Config.BASE_DEEPLINK}${data.productId}
            """.trimIndent()
        )
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, "Ecommerce")
    startActivity(context, shareIntent, null)
}

fun DataDetailProduct.toWishlist(selectedVariant: Int): WishlistEntity {
    return WishlistEntity(
        id = this.productId ?: System.currentTimeMillis().toString(),
        image = this.image?.get(0),
        productName = this.productName,
        productPrice = this.productPrice,
        store = this.store,
        productRating = this.productRating,
        brand = this.brand,
        sale = this.sale,
        stock = this.stock,
        variantPrice = this.productVariant?.get(selectedVariant)?.variantPrice?.plus(
            this.productPrice ?: 0
        ),
        variantName = this.productVariant?.get(selectedVariant)?.variantName
    )
}

fun DataDetailProduct.toCart(selectedVariant: Int): CartEntity {
    return CartEntity(
        id = this.productId ?: System.currentTimeMillis().toString(),
        image = this.image?.get(0),
        productName = this.productName,
        productPrice = this.productPrice,
        brand = this.brand,
        store = this.store,
        stock = this.stock,
        variantPrice = this.productVariant?.get(selectedVariant)?.variantPrice?.plus(
            this.productPrice ?: 0
        ),
        variantName = this.productVariant?.get(selectedVariant)?.variantName
    )
}

private fun analyticsEvent(
    analytics: FirebaseAnalytics,
    firebaseEvent: String,
    item: DataDetailProduct,
    selectedVariant: Int
) {
    analytics.logEvent(firebaseEvent) {
        param(
            FirebaseAnalytics.Param.ITEMS,
            bundleOf(
                FirebaseAnalytics.Param.ITEM_ID to item.productId,
                FirebaseAnalytics.Param.ITEM_NAME to item.productName,
                FirebaseAnalytics.Param.ITEM_BRAND to item.brand,
                FirebaseAnalytics.Param.ITEM_VARIANT to item.productVariant?.get(selectedVariant)?.variantName
            )
        )
        param(FirebaseAnalytics.Param.CURRENCY, "Rp")
        param(
            FirebaseAnalytics.Param.VALUE,
            (item.productVariant?.get(selectedVariant)?.variantPrice ?: 0).toDouble()
        )
    }
}
