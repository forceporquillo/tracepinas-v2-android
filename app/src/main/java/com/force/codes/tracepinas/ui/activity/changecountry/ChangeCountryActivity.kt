/*
 * Created by Force Porquillo on 9/9/20 12:54 PM
 */

package com.force.codes.tracepinas.ui.activity.changecountry

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.force.codes.tracepinas.R
import com.force.codes.tracepinas.data.entities.PerCountry
import com.force.codes.tracepinas.databinding.ActivityListViewBinding
import com.force.codes.tracepinas.di.factory.ViewModelProviderFactory
import com.force.codes.tracepinas.ui.base.BaseActivity
import com.force.codes.tracepinas.util.adapter.AdapterListener
import com.force.codes.tracepinas.util.adapter.BaseListAdapter
import com.force.codes.tracepinas.util.views.ItemDecoration
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ChangeCountryActivity : BaseActivity(), AdapterListener {

  private lateinit var binding: ActivityListViewBinding

  @Inject lateinit var factory: ViewModelProviderFactory

  private val _list = ArrayList<PerCountry>()

  private var _lastItemId = 2131296607

  private val activityViewModel: ChangeCountryViewModel by lazy {
    ViewModelProvider(this, factory).get(ChangeCountryViewModel::class.java)
  }

  private val _adapter = BaseListAdapter<PerCountry>(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityListViewBinding.inflate(layoutInflater).apply {
      listActivity = this@ChangeCountryActivity
      lifecycleOwner = this@ChangeCountryActivity
      viewModel = activityViewModel
      setContentView(root)
      executePendingBindings()
      setSupportActionBar(toolbar)
    }

    supportActionBar?.let {
      it.apply {
        setDisplayShowHomeEnabled(true)
        setDisplayHomeAsUpEnabled(true)
        setDisplayShowTitleEnabled(false)
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    binding.unbind()
  }

  override fun onStart() {
    super.onStart()
    activityViewModel.orderListViewBy(true).observe(this, { list ->
      Timber.e("list size: ${list.size}")
      _adapter.submitList(list.also { _list.addAll(it) })
      binding.listRecycler.apply {
        layoutManager = LinearLayoutManager(this@ChangeCountryActivity)
        addItemDecoration(
          ItemDecoration(
            this@ChangeCountryActivity,
            ItemDecoration.VERTICAL_LIST,
            0)
        )
        adapter = _adapter
      }
    })
  }

  override fun onGetAdapterPosition(position: Int) {
    lifecycleScope.launch { dataStoreUtil.storePrimaryCountry(_list[position].country) }
    finish()
  }

  override fun onOptionsItemSelected(
    item: MenuItem,
  ): Boolean {
    var order = true
    when (item.itemId) {
      android.R.id.home -> {
        onBackPressed()
        return true
      }
      R.id.order_cases -> {
        if (_lastItemId != item.itemId) {
          _lastItemId = item.itemId
          order = true
        }
      }
      R.id.order_alphabetical -> {
        if (_lastItemId != item.itemId) {
          _lastItemId = item.itemId
          order = false
        }
      }
    }
    activityViewModel.apply { observeChanges(order) }
    binding.invalidateAll()
    return super.onOptionsItemSelected(item)
  }

  private fun observeChanges(order: Boolean) {
    activityViewModel.orderListViewBy(order).observe(this, { _adapter.submitList(it) })
  }

  override fun onCreateOptionsMenu(
    menu: Menu?,
  ): Boolean {
    menuInflater.inflate(R.menu.menu_items, menu)
    val searchView = menu!!.findItem(R.id.action_search)
      .actionView as SearchView
    searchView.maxWidth = Int.MAX_VALUE
    return super.onCreateOptionsMenu(menu)
  }
}