package com.dipanshu.videoplayerdemo.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("imgResId")
fun setImgResId(imgView: ImageView, resId: Drawable) {
    imgView.setImageDrawable(resId)
}