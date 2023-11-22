package com.agungtriu.ecommerce.data

import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.helper.ViewState
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun insertCart(cartEntity: CartEntity): Flow<ViewState<String>>
    suspend fun updateCart(cartEntity: CartEntity)
    suspend fun updateCartsSelected(isSelected: Boolean)
    suspend fun deleteCart(cartEntity: CartEntity)
    suspend fun deleteCartsSelected()
    fun getCarts(): Flow<List<CartEntity>?>
    fun getCartById(id: String): Flow<CartEntity?>

}