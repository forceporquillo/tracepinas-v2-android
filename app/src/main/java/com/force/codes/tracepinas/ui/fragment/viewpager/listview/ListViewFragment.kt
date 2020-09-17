/*
 * Created by Force Porquillo on 9/10/20 1:16 PM
 */

package com.force.codes.tracepinas.ui.fragment.viewpager.listview

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.force.codes.tracepinas.R
import com.force.codes.tracepinas.util.constants.LIST_VIEW
import com.force.codes.tracepinas.util.views.PagedListAdapter
import com.force.codes.tracepinas.util.views.AdapterListener
import com.force.codes.tracepinas.data.entities.PerCountry
import com.force.codes.tracepinas.databinding.FragmentListViewBinding
import com.force.codes.tracepinas.di.factory.ViewModelProviderFactory
import com.force.codes.tracepinas.ui.base.BaseFragment
import com.force.codes.tracepinas.util.views.setupRefreshLayout
import timber.log.Timber
import javax.inject.Inject

class ListViewFragment : BaseFragment(
  R.layout.fragment_list_view
), AdapterListener {

  private lateinit var binding: FragmentListViewBinding

  @Inject lateinit var factory: ViewModelProviderFactory

  private lateinit var listAdapter: PagedListAdapter<PerCountry>

  private val listViewModel: ListViewModel by lazy {
    ViewModelProvider(this, factory).get(ListViewModel::class.java)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentListViewBinding.bind(view)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    binding.apply {
      lifecycleOwner = this@ListViewFragment.viewLifecycleOwner
      viewmodel = listViewModel
    }

    setupListAdapter()
    setupRefreshLayout(binding.swipeFresh, null)

    listViewModel.items.observe(viewLifecycleOwner, {
      listAdapter.submitList(it)

      it?.let {
        Timber.e("Data ${it.size}")
      }

      binding.recyclerView.apply {
        layoutManager = LinearLayoutManager(context!!)
        adapter = listAdapter
      }
    })
  }

  private fun setupListAdapter() {
    listAdapter = PagedListAdapter(LIST_VIEW, this)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding.unbind()
  }

  override fun onGetAdapterPosition(position: Int) {
    Toast.makeText(context!!, "Index clicked is at $position", Toast.LENGTH_LONG).show()
  }
}