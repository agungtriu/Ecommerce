package com.agungtriu.ecommerce.core.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCart(cartEntity: CartEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCart(cartEntity: CartEntity)

    @Query("UPDATE carts SET isSelected=:isSelected")
    suspend fun updateCartsSelected(isSelected: Boolean)

    @Query("SELECT * FROM carts")
    fun selectCarts(): Flow<List<CartEntity>?>

    @Query("SELECT * FROM carts WHERE isSelected = :isSelected")
    fun selectCartsIsSelected(isSelected: Boolean): Flow<List<CartEntity>>

    @Query("SELECT * FROM carts WHERE id = :id")
    fun selectCartById(id: String): Flow<CartEntity?>

    @Query("SELECT SUM(variantPrice*quantity) FROM carts WHERE isSelected = 1")
    fun selectTotalPay(): Flow<Int?>

    @Query("SELECT COUNT(*) FROM carts")
    fun selectQuantity(): Flow<Int?>

    @Query("DELETE FROM carts WHERE isSelected = 1")
    suspend fun deleteCartsSelected()

    @Delete
    suspend fun deleteCart(cartEntity: CartEntity)
}