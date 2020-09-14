/*
 * Created by Force Porquillo on 9/12/20 8:27 AM
 */

package com.force.codes.tracepinas.util

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.force.codes.tracepinas.data.entities.PerCountry

/**
 * PagedList DiffUtil
 */

open class DiffUtilComparator<T> : ItemCallback<T>() {
  override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
    when (oldItem) {
      is PerCountry -> {
        return (oldItem as PerCountry).country == (newItem as PerCountry).country
      }
    }
    throw ClassCastException()
  }

  @SuppressLint("DiffUtilEquals")
  override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
    when (oldItem) {
      is PerCountry -> {
        return (oldItem as PerCountry).cases == (newItem as PerCountry).cases
      }
    }
    throw ClassCastException()
  }
}