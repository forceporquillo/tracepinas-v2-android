/*
 * Created by Force Porquillo on 9/15/20 12:49 AM
 */

/*
 * Created by Force Porquillo on 9/15/20 12:34 AM
 */

package com.force.codes.tracepinas.util.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.force.codes.tracepinas.util.constants.LIST_VIEW
import com.force.codes.tracepinas.util.constants.SUPPRESS
import com.force.codes.tracepinas.custom.ListViewHolder
import com.force.codes.tracepinas.databinding.PerCountryListBinding
import com.force.codes.tracepinas.util.DiffUtilComparator

open class PagedListAdapter<T>(
  private val holderType: Int,
  private val listener: GenericPagedListListener,
) : PagedListAdapter<T, ViewHolder>(DiffUtilComparator<T>()) {

  override fun onCreateViewHolder(
    parent: ViewGroup, viewType: Int,
  ): ViewHolder {
    val inflater = LayoutInflater.from(parent.context)

    when (holderType) {
      LIST_VIEW -> {
        val binding = PerCountryListBinding.inflate(inflater, parent, false)
        return ListViewHolder(binding, listener)
      }
      else -> throw IllegalArgumentException()
    }
  }

  @Suppress(SUPPRESS)
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    (holder as Binder<T>).bind(getItemPositionAt(position)!!)
  }

  @Suppress(SUPPRESS)
  private fun <T> getItemPositionAt(position: Int): T? {
    return getItem(position) as T?
  }

  internal interface Binder<T> {
    fun bind(data: T)
  }
}

interface GenericPagedListListener {
  fun onGetAdapterPosition(position: Int)
}


