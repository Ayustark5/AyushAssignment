package com.ayustark.ayushassignment.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database([UserEntity::class, CartEntity::class], version = 6)
abstract class SwaadDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun cartDao(): CartDao
}