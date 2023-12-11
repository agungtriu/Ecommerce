package com.agungtriu.ecommerce.data

import com.agungtriu.ecommerce.core.remote.model.response.stockUnavailableError
import com.agungtriu.ecommerce.core.room.AppDatabase
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.helper.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CartRepositoryImp @Inject constructor(private val appDatabase: AppDatabase) : CartRepository {

    override fun insertCart(cartEntity: CartEntity): Flow<ViewState<String>> = flow {
        val check = appDatabase.cartDao().selectCartById(cartEntity.id).first()
        if (check != null) {
            if ((check.stock ?: 0) > (check.quantity ?: 0)) {
                updateCart(
                    cartEntity = CartEntity(
                        id = cartEntity.id,
                        image = cartEntity.image,
                        productName = cartEntity.productName,
                        productPrice = cartEntity.productPrice,
                        store = cartEntity.store,
                        stock = cartEntity.stock,
                        variantPrice = cartEntity.variantPrice,
                        variantName = cartEntity.variantName,
                        quantity = check.quantity?.plus(1),
                        isSelected = check.isSelected
                    )
                )
                emit(ViewState.Success("quantity"))
            } else {
                emit(ViewState.Error(stockUnavailableError))
            }
        } else {
            appDatabase.cartDao().insertCart(cartEntity)
            emit(ViewState.Success("cart"))
        }
    }

    override suspend fun updateCart(cartEntity: CartEntity) {
        appDatabase.cartDao().updateCart(cartEntity)
    }

    override suspend fun updateCartsSelected(isSelected: Boolean) {
        appDatabase.cartDao().updateCartsSelected(isSelected)
    }

    override suspend fun deleteCart(cartEntity: CartEntity) {
        appDatabase.cartDao().deleteCart(cartEntity)
    }

    override suspend fun deleteCartsSelected() {
        appDatabase.cartDao().deleteCartsSelected()
    }

    override fun getCarts(): Flow<List<CartEntity>?> = appDatabase.cartDao().selectCarts()

    override fun getCartById(id: String): Flow<CartEntity?> =
        appDatabase.cartDao().selectCartById(id)
}
