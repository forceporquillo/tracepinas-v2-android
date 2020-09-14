/*
 * Created by Force Porquillo on 9/14/20 9:42 AM
 */

package com.force.codes.tracepinas.ui.activity.changecountry

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.force.codes.tracepinas.data.entities.PerCountry

@BindingAdapter("app:items")
fun setItems(
  listView: RecyclerView,
  items: List<PerCountry>?
) {
  items?.let {
    (listView.adapter as ChangeCountryAdapter).updateData(items)
  }
}