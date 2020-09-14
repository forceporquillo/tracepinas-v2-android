/*
 * Created by Force Porquillo on 9/10/20 1:16 PM
 */

package com.force.codes.tracepinas.ui.fragment.viewpager.listview

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.force.codes.tracepinas.R
import com.force.codes.tracepinas.util.constants.LIST_VIEW
import com.force.codes.tracepinas.util.views.PagedListAdapter
import com.force.codes.tracepinas.util.views.GenericPagedListListener
import com.force.codes.tracepinas.data.entities.PerCountry
import com.force.codes.tracepinas.databinding.FragmentListViewBinding
import com.force.codes.tracepinas.di.factory.ViewModelProviderFactory
import com.force.codes.tracepinas.ui.base.BaseFragment
import timber.log.Timber
import javax.inject.Inject

class ListViewFragment : BaseFragment(
  R.layout.fragment_list_view
), GenericPagedListListener {

  private lateinit var binding: FragmentListViewBinding

  @Inject lateinit var factory: ViewModelProviderFactory

  private var adapter2 = PagedListAdapter<PerCountry>(LIST_VIEW,this)

  private val viewModel: ListViewModel by lazy {
    ViewModelProvider(this, factory).get(ListViewModel::class.java)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentListViewBinding.bind(view).apply {
      lifecycleOwner = this@ListViewFragment
      swipeFresh.setColorSchemeColors(
        ContextCompat.getColor(context!!, R.color.blue)
      )
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    viewModel.dataFromDatabase?.observe(viewLifecycleOwner, {
      adapter2.submitList(it)

      it?.let {
        Timber.e("Data ${it.size}")
      }

      binding.recyclerView.apply {
        layoutManager = LinearLayoutManager(context!!)
        adapter = adapter2
      }
    })
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding.unbind()
  }

  override fun onGetAdapterPosition(position: Int) {
    Toast.makeText(context!!, "Index clicked is at $position", Toast.LENGTH_LONG).show()
  }

}