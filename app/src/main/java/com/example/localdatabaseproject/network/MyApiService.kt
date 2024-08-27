package com.example.localdatabaseproject.network

import com.example.localdatabaseproject.di.ApiConstant
import com.example.localdatabaseproject.models.exercises.ExerciseResult
import com.example.localdatabaseproject.models.reels.ReelsResult
import com.example.localdatabaseproject.models.reels.UserIdResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MyApiService {


    @GET(ApiConstant.EXERCICES)
    suspend fun getExercises(): List<ExerciseResult>

    @GET(ApiConstant.EXERCISES_NAMES + "/{exerciseName}")
    suspend fun searchExercises(
        @Path("exerciseName") exerciseName: String
    ): List<ExerciseResult>

    @GET(ApiConstant.WEB_USER_REEL + "{userid}")
    suspend fun getInstaReels(
        @Path("userid") userID: Int,
        @Query(ApiConstant.MAX_ID) maxId: String,
        @Query(ApiConstant.NOCORSE) noCorse: Boolean
    ): ReelsResult

    @GET(ApiConstant.WEB_GET_USER_ID + "{username}")
    suspend fun getUserID(
        @Path("username") userName: String
    ): UserIdResult

}