package com.example.localdatabaseproject.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localdatabaseproject.models.ProductList
import com.example.localdatabaseproject.models.User
import com.example.localdatabaseproject.repository.RoomRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class RoomViewModel(private val repo: RoomRepo) : ViewModel() {
    private val _userlist = MutableLiveData<List<User>>()
    val userList get() = _userlist
    private val _productlist = MutableLiveData<List<ProductList>>()
    val productList get() = _productlist


    private val _signupSuccess = MutableLiveData<Boolean>()
    val signupSuccess: LiveData<Boolean> get() = _signupSuccess

    private val _loginCheckProcess = MutableLiveData<Boolean>()
    val loginCheckProcess: LiveData<Boolean> get() = _loginCheckProcess


    fun getUserList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _userlist.postValue(repo.getAllUsers())
                } catch (e: Exception) {
                    Log.e("roshan", "getUserList from viewmodel: ${e.localizedMessage ?: "error in somewhere"}")
                }
            }
        }
    }

    fun getAllProductList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _productlist.postValue(repo.getAllProductList())
                } catch (e: Exception) {
                    Log.e("roshan", "getproductlist from viewmodel: ${e.localizedMessage ?: "error in somewhere"}")
                }
            }
        }
    }


    fun loginCheck(user: User){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    _loginCheckProcess.postValue(repo.loginuserCheck(user.email.lowercase(Locale.ROOT),user.password))
                }catch (e:Exception){
                    Log.e(
                        "roshan",
                        "getUserList from viewmodel: ${e.localizedMessage ?: "error in somewhere"}"
                    )
                }
            }
        }
    }

    fun signUp(user: User) {
        viewModelScope.launch {
            try {
                val userExists = repo.checkWhileSignup(user.email)
                if (userExists) {
                    _signupSuccess.postValue(false)
                } else {
                    signUpProcess(user)
                    _signupSuccess.postValue(true)
                }
            } catch (e: Exception) {
                // Handle the exception appropriately
                // For example, log the error
                _signupSuccess.postValue(false)
                Log.e("YourTag", "Error while signing up: ${e.message}")
                // Handle the error case as appropriate for your application
            }
        }
    }


    private fun signUpProcess(user: User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    repo.insertUser(user)
                } catch (e: Exception) {
                    Log.e("roshan", "signup error: ${e.localizedMessage ?: "error in somewhere"}")
                }
            }
        }
    }

    fun insertProduct(productList: ProductList, result: (Boolean) ->Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    repo.insertProduct(productList)
                    result.invoke(true)
                } catch (e: Exception) {
                    Log.e("roshan", "signup error: ${e.localizedMessage ?: "error in somewhere"}")
                    result.invoke(false)
                }
            }
        }
    }

}