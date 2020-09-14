/*
 * Created by Force Porquillo on 9/12/20 6:40 AM
 */

package com.force.codes.tracepinas.custom

import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.force.codes.tracepinas.BR
import com.force.codes.tracepinas.util.views.PagedListAdapter.Binder
import com.force.codes.tracepinas.data.entities.PerCountry
import com.force.codes.tracepinas.databinding.PerCountryListBinding
import com.force.codes.tracepinas.util.views.GenericPagedListListener

class ListViewHolder(
  private val binding: PerCountryListBinding,
  private val listListener: GenericPagedListListener
) : ViewHolder(binding.root), Binder<PerCountry>, OnClickListener {

  override fun bind(data: PerCountry) {
    binding.apply {
      details = data
      setVariable(BR.listItems, data)
      executePendingBindings()
    }
  }

  init {
    binding.root.setOnClickListener(this)
  }

  override fun onClick(v: View?) {
    listListener.onGetAdapterPosition(adapterPosition)
  }
}

