/*
 * Created by Force Porquillo on 8/8/20 9:30 PM
 * FEU Institute of Technology
 * Copyright (c) 2020.  All rights reserved.
 * Last modified 8/8/20 9:30 PM
 */
package com.force.codes.tracepinas.ui.adapter.viewholder

import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.force.codes.tracepinas.BR
import com.force.codes.tracepinas.data.model.per_country.PerCountry
import com.force.codes.tracepinas.databinding.ListViewItemsBinding
import com.force.codes.tracepinas.util.events.StackEventListener.OnGetAdapterPosition

class ListViewHolder(
  private val binding: ListViewItemsBinding,
  private val callback: OnGetAdapterPosition
) : ViewHolder(binding.root), OnClickListener {
  fun setBinding(details: PerCountry?) {
    binding.listItems = details
    binding.setVariable(BR.listItems, details)
    binding.executePendingBindings()
  }

  override fun onClick(v: View) {
    callback.onItemClicked(adapterPosition)
  }

  init {
    binding.root.setOnClickListener(this)
  }
}