package com.example.localdatabaseproject.utils

import android.widget.ImageView
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

}