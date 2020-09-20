/*
 * Created by Force Porquillo on 9/15/20 2:31 AM
 */

package com.force.codes.tracepinas.util.views

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.facebook.shimmer.ShimmerFrameLayout
import com.force.codes.tracepinas.R

@BindingAdapter("image")
fun setImage(image: ImageView, url: String?) {
  Glide.with(image.context)
    .asBitmap()
    .apply(
      RequestOptions()
        .fitCenter()
        .override(125, 125)
    )
    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
    .placeholder(R.drawable.cupertino_loading)
    .load(url)
    .into(image)
}

@BindingAdapter("showEffect")
fun setShimmerEffect(shimmer: ShimmerFrameLayout, show: Boolean) {
  shimmer.apply {
    if (show) startShimmer() else stopShimmer()
  }
}

@BindingAdapter("fillText")
fun fillTextColor(
  bottomTitle: TextView,
  fillColor: Boolean,
) {
  fillColor.let {
    if (it) {
      bottomTitle.setTextColor(Color.rgb(50, 121, 210))
    } else {
      bottomTitle.setTextColor(Color.rgb(191, 191, 191))
    }
  }
}

@BindingAdapter("bottomIconRes")
fun setImageResource(image: ImageView, @DrawableRes imageId: Int) {
  image.setBackgroundResource(imageId)
}