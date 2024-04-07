package com.example.localdatabaseproject.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.localdatabaseproject.models.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<User>


    @Query("SELECT EXISTS (SELECT 1 FROM User WHERE email = :email AND (:password IS NULL OR password = :password) LIMIT 1)")
    suspend fun isUserExists(email: String, password: String? = null): Boolean



}
