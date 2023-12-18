package com.agungtriu.ecommerce.core.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCart(cartEntity: CartEntity)

    @Query("UPDATE carts SET quantity=:quantity, isSelected=:isSelected WHERE id=:id")
    suspend fun updateCart(id: String, quantity: Int, isSelected: Boolean)

    @Query("UPDATE carts SET isSelected=:isSelected")
    suspend fun updateCartsSelected(isSelected: Boolean)

    @Query("SELECT * FROM carts")
    fun selectCarts(): Flow<List<CartEntity>?>

    @Query("SELECT * FROM carts WHERE id = :id")
    fun selectCartById(id: String): Flow<CartEntity?>

    @Query("SELECT COUNT(*) FROM carts")
    fun selectCountCart(): Flow<Int>

    @Query("DELETE FROM carts WHERE isSelected = 1")
    suspend fun deleteCartsSelected()

    @Delete
    suspend fun deleteCart(cartEntity: CartEntity)
}
