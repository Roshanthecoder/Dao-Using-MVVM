package com.example.localdatabaseproject.repository

import android.util.Log
import com.example.localdatabaseproject.models.exercises.ExerciseResult
import com.example.localdatabaseproject.models.reels.ReelsResult
import com.example.localdatabaseproject.models.reels.UserIdResult
import com.example.localdatabaseproject.network.MyApiService
import javax.inject.Inject

class ApiRepo @Inject constructor(private val myApiService: MyApiService) {

    suspend fun getExerises(): List<ExerciseResult> {
        return try {
            myApiService.getExercises()
        } catch (e: Exception) {
            Log.e("roshan", "_exercisesList: ${e.localizedMessage ?: "something went wrong"}")
            emptyList()
        }
    }

    suspend fun searchExerises(keyword: String): List<ExerciseResult> {
        return try {
            myApiService.searchExercises(keyword)
        } catch (e: Exception) {
            Log.e("roshan", "_exercisesList: ${e.localizedMessage ?: "something went wrong"}")
            emptyList()
        }
    }

    suspend fun getInstaReels(userID: Int, maxId: String, noCorse: Boolean): ReelsResult? {
        return try {
            myApiService.getInstaReels(userID, maxId, noCorse)
        } catch (e: Exception) {
            // Handle the exception or log it
            Log.e("ReelsRepository", "Error fetching Instagram reels", e)
            null // or throw a custom exception if you want to handle it differently
        }
    }

    suspend fun getUserId(userKeyword: String): UserIdResult? {
        return try {
            myApiService.getUserID(userKeyword)
        } catch (e: Exception) {
            // Handle the exception or log it
            Log.e("ReelsRepository", "Error fetching Instagram reels", e)
            null // or throw a custom exception if you want to handle it differently
        }
    }
}