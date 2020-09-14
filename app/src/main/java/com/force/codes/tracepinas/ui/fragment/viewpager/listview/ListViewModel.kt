/*
 * Created by Force Porquillo on 9/10/20 1:16 PM
 */

/*
 * Created by Force Porquillo on 9/10/20 1:02 PM
 */

package com.force.codes.tracepinas.ui.fragment.viewpager.listview

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.PagedList.Config.Builder
import com.force.codes.tracepinas.data.entities.PerCountry
import com.force.codes.tracepinas.ui.base.BaseViewModel
import com.force.codes.tracepinas.util.constants.PageListConstants.PAGE_MAX_SIZE
import com.force.codes.tracepinas.util.constants.PageListConstants.PAGE_SIZE
import com.force.codes.tracepinas.data.repository.ListViewRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListViewModel
@Inject internal constructor(
  private val listRepository: ListViewRepository
) : BaseViewModel() {

  companion object {
    private val config = Builder()
      .setPageSize(PAGE_SIZE)
      .setMaxSize(PAGE_MAX_SIZE)
      .setEnablePlaceholders(false)
      .build()
  }

  init {
    viewModelScope.launch {
      listRepository.updateDataFromRemoteDataSource()
    }
  }

  fun update() {
    viewModelScope.launch {
      listRepository.updateDataFromRemoteDataSource()
    }
  }

  var liveData: LiveData<PagedList<PerCountry?>?>? = null

  val dataFromDatabase: LiveData<PagedList<PerCountry?>?>?
    get() = if (liveData == null) {
      listRepository.getFromDataSource(config)
        .also { liveData = it }
    } else liveData

}