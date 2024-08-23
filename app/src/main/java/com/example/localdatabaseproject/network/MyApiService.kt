package com.example.localdatabaseproject.network

import com.example.localdatabaseproject.di.ApiConstant
import com.example.localdatabaseproject.models.exercises.ExerciseResult
import retrofit2.http.GET
import retrofit2.http.Path

interface MyApiService {


    @GET(ApiConstant.EXERCICES)
    suspend fun getExercises(): List<ExerciseResult>

    @GET(ApiConstant.EXERCISES_NAMES+"/{exerciseName}")
    suspend fun searchExercises(
        @Path("exerciseName") exerciseName: String
    ): List<ExerciseResult>

}