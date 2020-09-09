/*
 * Created by Force Porquillo on 8/8/20 9:30 PM
 * FEU Institute of Technology
 * Copyright (c) 2020.  All rights reserved.
 * Last modified 30/8/20 9:30 PM
 */
package com.force.codes.tracepinas.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.force.codes.tracepinas.data.model.per_country.PerCountry
import com.force.codes.tracepinas.databinding.ListViewItemsBinding
import com.force.codes.tracepinas.ui.adapter.viewholder.ListViewHolder
import com.force.codes.tracepinas.util.events.StackEventListener.OnGetAdapterPosition

import java.util.ArrayList

class ListViewAdapter(
  private val callback: OnGetAdapterPosition,
) : Adapter<ListViewHolder>() {

  private var detailsList: List<PerCountry?>

  fun updateData(
    detailsList: List<PerCountry?>,
  ) {
    this.detailsList = detailsList
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int,
  ): ListViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val binding = ListViewItemsBinding.inflate(inflater, parent, false)
    return ListViewHolder(binding, callback)
  }

  override fun onBindViewHolder(
    holder: ListViewHolder,
    position: Int,
  ) {
    holder.setBinding(detailsList[position])
  }

  override fun getItemCount(): Int {
    return detailsList.size
  }

  init {
    detailsList = ArrayList()
  }
}