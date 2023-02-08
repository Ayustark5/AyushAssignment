package com.ayustark.ayushassignment.database

import androidx.room.*

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCartItem(item: CartEntity): Long

    @Query("delete from cart_tb where id == :itemId")
    suspend fun removeCartItem(itemId: String): Int

    @Query("select * from cart_tb order by id")
    suspend fun getCartItems(): List<CartEntity>

    @Query("delete from cart_tb")
    suspend fun emptyCart(): Int

}