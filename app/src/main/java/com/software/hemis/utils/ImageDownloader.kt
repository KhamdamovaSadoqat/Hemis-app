package com.software.hemis.utils


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.hemis.R
import com.github.twocoffeesoneteam.glidetovectoryou.GlideApp
import com.github.twocoffeesoneteam.glidetovectoryou.GlideApp.with
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou

object ImageDownloader {
    @SuppressLint("CheckResult")
    fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        @DrawableRes
        placeHolderRes: Int? = null,
        @DrawableRes
        errorRes: Int? = null,
    ) {
        val uri = Uri.parse(url)
        val separated = url.split(".")

        if (separated.last() == "svg") {
            GlideToVectorYou
                .init()
                .apply {
                    with(context)
                    if (placeHolderRes != null && errorRes != null)
                        setPlaceHolder(placeHolderRes, errorRes)
                    else
                        setPlaceHolder(R.drawable.ic_selected_day_background, R.drawable.ic_selected_day_background)
                    load(uri, imageView)
                }
        } else {
            Glide.with(context)
                .load(uri)
                .apply {
                    if (placeHolderRes != null)
                        placeholder(placeHolderRes)
                    else
                        placeholder(R.drawable.ic_selected_day_background)
                    if (errorRes != null)
                        error(errorRes)
                    else
                        error(R.drawable.ic_selected_day_background)
                            .into(imageView)
                }
        }
    }
}