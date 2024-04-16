package com.example.localdatabaseproject.models

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductList(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val pName:String,
    val pQty:Int,
    val pRate:Int,
    val pImage:String?=null,
    var manualQty:Int=0
)
