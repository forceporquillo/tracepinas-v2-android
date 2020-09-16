/*
 * Created by Force Porquillo on 9/15/20 2:31 AM
 */

package com.force.codes.tracepinas.util.views

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.force.codes.tracepinas.R

@BindingAdapter("image")
fun setImage(image: ImageView, url: String?) {
  Glide.with(image.context)
    .asBitmap()
    .apply(RequestOptions()
      .fitCenter()
      .override(125, 125))
    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
    .placeholder(R.drawable.cupertino_loading)
    .load(url)
    .into(image)
}