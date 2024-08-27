package com.example.localdatabaseproject.utils

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

object CommonUtils {

    fun ImageView.loadGif(url: String) {
        Glide.with(this.context)
            .asGif() // Load as GIF
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache the GIF
            .into(this)
    }

    fun showtoast(context:Context,msg:String){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }

}