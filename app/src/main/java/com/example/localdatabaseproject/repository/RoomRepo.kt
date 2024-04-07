package com.example.localdatabaseproject.repository

import android.util.Log
import com.example.localdatabaseproject.models.User
import com.example.localdatabaseproject.roomdb.UserDao

class RoomRepo(private val dao: UserDao) {

    suspend fun insertUser(user: User) {
        try {
            dao.insertUser(user)
        } catch (e: Exception) {
            Log.e("roshan", "insertUser: ${e.localizedMessage ?: "something went woring"}")
        }
    }

    suspend fun getAllUsers(): List<User> {
        return try {
            dao.getAllUsers()
        } catch (e: Exception) {
            Log.e("roshan", "getAllUser: ${e.localizedMessage ?: "something went woring"}")
            emptyList()
        }
    }

    suspend fun checkWhileSignup(email: String,password:String?=null): Boolean {
        return try {
            dao.isUserExists(email,password)
        } catch (e: Exception) {
            Log.e("YourTag", "Error while checking user existence: ${e.message}")
            false
        }
    }
}