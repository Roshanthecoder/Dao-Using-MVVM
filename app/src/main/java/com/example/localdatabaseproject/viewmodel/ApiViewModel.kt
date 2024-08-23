package com.example.localdatabaseproject.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localdatabaseproject.models.exercises.ExerciseResult
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


    fun searchExercises(keywords:String) {
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
