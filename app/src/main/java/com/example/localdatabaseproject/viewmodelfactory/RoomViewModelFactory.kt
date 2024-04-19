package com.example.localdatabaseproject.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.localdatabaseproject.repository.RoomRepo
import com.example.localdatabaseproject.viewmodel.RoomViewModel

/*
class RoomViewModelFactory(private val repo: RoomRepo):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RoomViewModel::class.java)){
            return RoomViewModel(repo) as T
        }
       throw IllegalArgumentException("Unknown Viewmodel Class")
    }
}
*/
