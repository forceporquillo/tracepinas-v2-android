/*
 * Created by Force Porquillo on 9/17/20 12:45 PM
 */

package com.force.codes.tracepinas.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.force.codes.tracepinas.data.entities.PerCountry
import com.force.codes.tracepinas.util.constants.LIST_VIEW
import com.force.codes.tracepinas.databinding.PerCountryListBinding

class PagedListAdapter<T>(
  private val holderType: Int,
  private val listener: AdapterListener,
) : PagedListAdapter<T, ViewHolder>(DiffUtilComparator<T>()) {

  override fun onCreateViewHolder(
    parent: ViewGroup, viewType: Int,
  ): ViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val binding : ViewDataBinding
    when (holderType) {
      LIST_VIEW -> binding = PerCountryListBinding.inflate(inflater, parent, false)
      else -> throw ClassCastException()
    }
    return ViewHolderFactory<PerCountry>(binding, listener)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    (holder as Binder<*>).bind(getItem(position))
  }
}

internal interface Binder<T> {
  fun bind(data: Any?)
}

interface AdapterListener {
  fun onGetAdapterPosition(position: Int)
}


