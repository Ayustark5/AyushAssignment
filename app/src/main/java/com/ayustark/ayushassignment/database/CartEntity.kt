package com.ayustark.ayushassignment.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("cart_tb")
class CartEntity {

    @PrimaryKey
    var id: String = ""

    var costForOne: String = "0"
    var name: String = ""
    var restaurantId: String = ""
    var quantity: Int = 0
}