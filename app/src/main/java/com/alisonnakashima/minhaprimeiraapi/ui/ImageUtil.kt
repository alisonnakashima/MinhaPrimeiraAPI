package com.alisonnakashima.minhaprimeiraapi.ui

import android.widget.ImageView
import com.alisonnakashima.minhaprimeiraapi.R
import com.squareup.picasso.Picasso

fun ImageView.loadUrl(imageUrl: String) {
    Picasso.get()
        .load(imageUrl)
        .placeholder(R.drawable.ic_download)
        .error(R.drawable.ic_error)
        .transform(CircleTransform())
        .into(this)
}