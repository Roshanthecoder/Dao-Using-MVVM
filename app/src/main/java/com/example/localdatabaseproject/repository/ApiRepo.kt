package com.example.localdatabaseproject.repository

import android.util.Log
import com.example.localdatabaseproject.models.exercises.ExerciseResult
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

    suspend fun searchExerises(keyword:String): List<ExerciseResult> {
        return try {
            myApiService.searchExercises(keyword)
        } catch (e: Exception) {
            Log.e("roshan", "_exercisesList: ${e.localizedMessage ?: "something went wrong"}")
            emptyList()
        }
    }
}