/*
 * Created by Force Porquillo on 9/17/20 12:45 PM
 */

package com.force.codes.tracepinas.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.force.codes.tracepinas.data.entities.PerCountry
import com.force.codes.tracepinas.databinding.ListViewItemsBinding

class GenericAdapter<T>(
  private val listener: AdapterListener,
) : Adapter<ViewHolder>() {

  private val asyncListDiffer = AsyncListDiffer(this, DiffUtilComparator<T>())

  fun submitList(list: List<T>) {
    asyncListDiffer.submitList(list)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val binding: ViewDataBinding
    when {
      asyncListDiffer.currentList.all { it is PerCountry } -> {
        binding = ListViewItemsBinding.inflate(inflater, parent, false)
      }
      else ->  throw ClassCastException()
    }
    return ViewHolderFactory<PerCountry>(binding, listener)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    asyncListDiffer.currentList[position]!!.let { (holder as Binder<*>).bind(it) }
  }

  override fun getItemCount(): Int {
    return asyncListDiffer.currentList.size
  }
}