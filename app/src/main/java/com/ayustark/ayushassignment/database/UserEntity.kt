package com.ayustark.ayushassignment.database

import android.util.Patterns
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users_tb",
    indices = [Index(value = ["email"], unique = true), Index(value = ["mobile"], unique = true)]
)
class UserEntity {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var name: String = ""
    var email: String = ""
    var mobile: String = ""
    var password: String = ""
    var address: String = ""

    fun validate(): Boolean {
        if (name.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() && Patterns.PHONE.matcher(mobile)
                .matches() && password.isNotBlank() && address.isNotBlank()
        )
            return true
        return false
    }
}