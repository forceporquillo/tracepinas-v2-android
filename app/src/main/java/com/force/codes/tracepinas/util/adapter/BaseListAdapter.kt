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
import com.force.codes.tracepinas.databinding.BottomBarItemBinding
import com.force.codes.tracepinas.databinding.ListViewItemsBinding
import com.force.codes.tracepinas.ui.activity.navhost.BottomBarItem
import com.force.codes.tracepinas.ui.activity.navhost.BottomViewHolder

class BaseListAdapter<T>(
  private val listener: AdapterListener,
  list: List<T>? = null
) : Adapter<ViewHolder>() {

  private val asyncListDiffer = AsyncListDiffer(this, DiffUtilComparator<T>())

  init {
    list.let { asyncListDiffer.submitList(it) }
  }

  fun submitList(list: List<T>) {
    asyncListDiffer.submitList(list)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val binding: ViewDataBinding = when {
      asyncListDiffer.currentList.all { it is PerCountry } -> {
        ListViewItemsBinding.inflate(inflater, parent, false)
      }
      else -> throw ClassCastException()
    }
    return ViewHolderFactory<PerCountry>(binding, listener)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    (holder as Binder<*>).bind(asyncListDiffer.currentList[position])
  }

  override fun getItemCount(): Int {
    return asyncListDiffer.currentList.size
  }
}