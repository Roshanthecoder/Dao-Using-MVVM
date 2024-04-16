package com.example.localdatabaseproject.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.localdatabaseproject.models.ProductList
import com.example.localdatabaseproject.models.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM User")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM ProductList")
    suspend fun getAllProducts(): List<ProductList>

    @Insert
    suspend fun inserProduct(productList: ProductList)

    @Query("SELECT EXISTS (SELECT 1 FROM User WHERE email = :email LIMIT 1)")
    suspend fun isUserExists(email: String): Boolean

    @Query("SELECT EXISTS (SELECT 1 FROM User WHERE email = :email AND password = :password LIMIT 1)")
    suspend fun isUserFound(email: String, password: String): Boolean


}
