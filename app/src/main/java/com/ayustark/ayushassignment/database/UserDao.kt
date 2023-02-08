package com.ayustark.ayushassignment.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createUser(user: UserEntity): Long

    @Query("select * from users_tb where id = :userId")
    suspend fun getCurrentUser(userId: Long): UserEntity

    @Query("select id from users_tb where mobile = :mobile and password = :password")
    suspend fun authenticate(mobile: String, password: String): List<Long>

}