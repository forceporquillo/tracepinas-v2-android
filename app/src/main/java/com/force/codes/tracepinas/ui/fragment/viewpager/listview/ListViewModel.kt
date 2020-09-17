/*
 * Created by Force Porquillo on 9/10/20 1:16 PM
 */


package com.force.codes.tracepinas.ui.fragment.viewpager.listview

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.PagedList.Config.Builder
import com.force.codes.tracepinas.data.entities.PerCountry
import com.force.codes.tracepinas.data.repository.ListViewRepository
import com.force.codes.tracepinas.ui.base.BaseViewModel
import com.force.codes.tracepinas.util.constants.PageListConstants.PAGE_MAX_SIZE
import com.force.codes.tracepinas.util.constants.PageListConstants.PAGE_SIZE
import com.haroldadmin.cnradapter.NetworkResponse.Success
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListViewModel
@Inject internal constructor(
  private val listRepository: ListViewRepository,
) : BaseViewModel() {

  private val _dataLoading = MutableLiveData<Boolean>()
  val dataLoading: LiveData<Boolean> = _dataLoading

  private val _network = MutableLiveData<Boolean>()
  val network: LiveData<Boolean> = _network.also {
    Transformations.map(_items) { it.isNotEmpty() }
  }

  private val size = listRepository.getFromDataSource(config).value?.size

  private val _forceUpdate = MutableLiveData(false)

  private val _items: LiveData<PagedList<PerCountry>> = _forceUpdate.switchMap { forceUpdate ->
    if (forceUpdate) {
      _dataLoading.value = true
      viewModelScope.launch {
        listRepository.getFromDataSource(config).distinctUntilChanged().switchMap { filterUpdate(it) }
        return@launch
      }
    }
    listRepository.getFromDataSource(config)
  }

  private fun filterUpdate(list: PagedList<PerCountry>?) : LiveData<PagedList<PerCountry>> {
    val source = listRepository.getFromDataSource(config)
    viewModelScope.launch {
      val result = listRepository.updateDataFromRemoteDataSource()
      if (result is Success || list?.size != 0) {
        _dataLoading.value = false
        _network.value = false
      } else {
        _dataLoading.value = true
        delay(10000)
        _dataLoading.value = false
        _network.value = true
      }
    }
    return source
  }

  val items: LiveData<PagedList<PerCountry>> = _items

  private fun update(size: Int?) {
    viewModelScope.launch {
      val result = listRepository.updateDataFromRemoteDataSource()
      if (result is Success || size != 0) {
        _dataLoading.value = false
        _network.value = false
      } else {
        _dataLoading.value = true
        delay(10000)
        _dataLoading.value = false
        _network.value = true
      }
    }
  }

  @MainThread
  fun loadData(forceUpdate: Boolean) {
    _forceUpdate.value = forceUpdate
  }

  fun refresh() {
    _forceUpdate.value = true
  }

  companion object {
    private val config = Builder()
      .setPageSize(PAGE_SIZE)
      .setMaxSize(PAGE_MAX_SIZE)
      .setEnablePlaceholders(false)
      .build()
  }

  init {
    loadData(true)
  }
}