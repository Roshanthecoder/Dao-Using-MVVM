package com.example.localdatabaseproject.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localdatabaseproject.models.exercises.ExerciseResult
import com.example.localdatabaseproject.models.reels.ReelsResult
import com.example.localdatabaseproject.models.reels.UserIdResult
import com.example.localdatabaseproject.repository.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ApiViewModel @Inject constructor(
    private val apiRepo: ApiRepo
) : ViewModel() {

    private val _exercisesList = MutableLiveData<List<ExerciseResult>>()
    val exercisesList: LiveData<List<ExerciseResult>> = _exercisesList
    val loader = MutableLiveData<Boolean>()

    private val _reelsResult = MutableLiveData<ReelsResult?>()
    val reelsResult: LiveData<ReelsResult?> get() = _reelsResult

    private val _userId = MutableLiveData<UserIdResult?>()
    val userID: LiveData<UserIdResult?> get() = _userId

    fun fetchInstaReels(userID: Int, maxId: String, noCorse: Boolean) {
        loader.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = apiRepo.getInstaReels(userID, maxId, noCorse)
                _reelsResult.postValue(result)
                loader.postValue(false)
            } catch (e: Exception) {
                // Handle any additional errors if needed
                Log.e("ReelsViewModel", "Error in ViewModel", e)
                loader.postValue(false)
            }
        }
    }

    fun getUserIdFromUser(userKeyword: String) {
        loader.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = apiRepo.getUserId(userKeyword)
                _userId.postValue(result)
                loader.postValue(false)
            } catch (e: Exception) {
                // Handle any additional errors if needed
                Log.e("ReelsViewModel", "Error in ViewModel", e)
                loader.postValue(false)
            }
        }
    }


    fun getExercises() {
        loader.postValue(true)
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = apiRepo.getExerises()  // Network call
                _exercisesList.postValue(response)
                loader.postValue(false)
            }
        } catch (e: Exception) {
            Log.e("ApiViewModel", "Error fetching exercises", e)
            loader.postValue(false)
        }
    }


    fun searchExercises(keywords: String) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = apiRepo.searchExerises(keywords)  // Network call
                _exercisesList.postValue(response)
            }
        } catch (e: Exception) {
            Log.e("ApiViewModel", "Error fetching exercises", e)
        }
    }
}
