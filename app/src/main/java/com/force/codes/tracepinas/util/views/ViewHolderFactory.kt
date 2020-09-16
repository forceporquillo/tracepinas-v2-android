/*
 * Created by Force Porquillo on 9/15/20 1:19 AM
 */

package com.force.codes.tracepinas.util.views

import android.view.View
import android.view.View.OnClickListener
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.force.codes.tracepinas.BR
import com.force.codes.tracepinas.data.entities.PerCountry
import com.force.codes.tracepinas.databinding.ListViewItemsBinding
import com.force.codes.tracepinas.databinding.PerCountryListBinding

class ViewHolderFactory<T>(
  private val binding: ViewDataBinding,
  private val listener: AdapterListener,
) : ViewHolder(binding.root), Binder<T>, OnClickListener {

  private val setListener = binding.root.setOnClickListener(this)

  override fun onClick(v: View?) {
    listener.onGetAdapterPosition(adapterPosition)
  }

  override fun bind(data: Any?) {
    when (data) {
      is PerCountry -> {
        if (binding is PerCountryListBinding) {
          binding.details = data
        } else if (binding is ListViewItemsBinding) {
          binding.listItems = data
        }

        binding.apply {
          setVariable(BR.listItems, data)
          executePendingBindings()
        }
      }
    }
  }

  init {
    setListener
  }
}