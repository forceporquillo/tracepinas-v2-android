/*
 * Created by Force Porquillo on 9/9/20 12:54 PM
 */

package com.force.codes.tracepinas.ui.activity.changecountry

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.force.codes.tracepinas.R
import com.force.codes.tracepinas.data.entities.PerCountry
import com.force.codes.tracepinas.databinding.ActivityListViewBinding
import com.force.codes.tracepinas.di.factory.ViewModelProviderFactory
import com.force.codes.tracepinas.ui.base.BaseActivity
import com.force.codes.tracepinas.util.views.AdapterListener
import com.force.codes.tracepinas.util.views.GenericAdapter
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

  private val _adapter = GenericAdapter<PerCountry>(this)

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

    activityViewModel.forceUpdate().observe(this, { list ->
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

  override fun onDestroy() {
    super.onDestroy()
    binding.unbind()
  }

  override fun onGetAdapterPosition(position: Int) {
    lifecycleScope.launch {
      _list[position].country.let {
        dataStoreUtil.storePrimaryCountry(it)
      }
      val item = _list[position].country
      Timber.e("Selected country $item")
    }
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
    activityViewModel.apply {
      orderListViewBy(order)
      observeChanges()
    }

    binding.invalidateAll()
    return super.onOptionsItemSelected(item)
  }

  private fun observeChanges() {
    activityViewModel.data.observe(this, { _adapter.submitList(it) })
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