/*
 * Created by Force Porquillo on 9/9/20 12:54 PM
 */

package com.force.codes.tracepinas.ui.activity.list_view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.force.codes.tracepinas.R
import com.force.codes.tracepinas.data.model.per_country.CountryInfo
import com.force.codes.tracepinas.data.model.per_country.PerCountry
import com.force.codes.tracepinas.databinding.ActivityListViewBinding
import com.force.codes.tracepinas.di.module.ViewModelProviderFactory
import com.force.codes.tracepinas.ui.adapter.ListViewAdapter
import com.force.codes.tracepinas.ui.base.BaseActivity
import com.force.codes.tracepinas.util.custom.ItemDecoration
import com.force.codes.tracepinas.util.events.StackEventListener.OnGetAdapterPosition
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Arrays
import javax.inject.Inject

class ListViewActivity : BaseActivity(), OnGetAdapterPosition {

  private lateinit var binding: ActivityListViewBinding

  private lateinit var detailsList: List<PerCountry?>

  private lateinit var listViewAdapter: ListViewAdapter

  @Inject lateinit var factory: ViewModelProviderFactory
  val list = ArrayList<PerCountry>()
  private var lastItemId = 2131296607

  private val viewModel: ListViewModel by lazy {
    ViewModelProvider(this, factory).get(ListViewModel::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityListViewBinding.inflate(layoutInflater).apply {
      listActivity = this@ListViewActivity
      lifecycleOwner = this@ListViewActivity
      toolbar.title = ""
      setContentView(this.root)
    }

    setSupportActionBar(binding.toolbar)

    listViewAdapter = ListViewAdapter(this)

    supportActionBar?.let {
      it.setDisplayShowHomeEnabled(true)
      it.setDisplayHomeAsUpEnabled(true)
    }

    binding.listRecycler.apply {
      layoutManager = LinearLayoutManager(
        this@ListViewActivity
      )
      addItemDecoration(
        ItemDecoration(
          this@ListViewActivity,
          ItemDecoration.VERTICAL_LIST,
          0
        )
      )
      adapter = listViewAdapter
      setHasFixedSize(true)
    }

    binding.hasPendingBindings()
      .apply {
        binding.executePendingBindings()
      }
  }

  override fun onStart() {
    super.onStart()
//    viewModel.getLiveData.observe(this, { details ->
//      listViewAdapter.updateData(details)
//      detailsList = details
//    })


    list.add(
      PerCountry(
      "qwerty",
      5,
      CountryInfo(
        523,
        231,
        "Awit"
      ),
      5,
      5,
      5,
      5,
      5,
      5,
      5,
      5,
      5,
      5,
      5,
      5,
      5,
      "Asia",
      5,
      5,
      5,
      5.5,
      5.5,
      5.5)
    )

    list.add(
      PerCountry(
        "asdghjkl",
        5,
        CountryInfo(
          523,
          231,
          "Awit"
        ),
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        "Asia",
        5,
        5,
        5,
        5.5,
        5.5,
        5.5)
    )

    list.add(
      PerCountry(
        "xxxxxerwerdf",
        5,
        CountryInfo(
          523,
          231,
          "Awit"
        ),
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        "Asia",
        5,
        5,
        5,
        5.5,
        5.5,
        5.5)
    )

    list.add(
      PerCountry(
        "gehsdf",
        5,
        CountryInfo(
          523,
          231,
          "Awit"
        ),
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        "Asia",
        5,
        5,
        5,
        5.5,
        5.5,
        5.5)
    )

    list.add(
      PerCountry(
        "berger",
        5,
        CountryInfo(
          523,
          231,
          "Awit"
        ),
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        "Asia",
        5,
        5,
        5,
        5.5,
        5.5,
        5.5)
    )
    listViewAdapter.updateData(list)
  }

  override fun onDestroy() {
    super.onDestroy()
    binding.unbind()
  }

  override fun onItemClicked(
    index: Int,
  ) {
    lifecycleScope.launch {
      list[index].country.let {
        dataStoreUtil.storePrimaryCountry(it)
      }
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
        if (lastItemId != item.itemId) {
          lastItemId = item.itemId
          Timber.e("cases")
          order = true
        }
      }
      R.id.order_alphabetical -> {
        if (lastItemId != item.itemId) {
          lastItemId = item.itemId
          Timber.e("alphabetical")
          order = false
        }
      }
    }
    viewModel.orderListViewBy(order)
    binding.invalidateAll()
    return super.onOptionsItemSelected(item)
  }

  override fun onCreateOptionsMenu(
    menu: Menu?,
  ): Boolean {
    menuInflater.inflate(R.menu.menu_items, menu)
    return true
  }
}