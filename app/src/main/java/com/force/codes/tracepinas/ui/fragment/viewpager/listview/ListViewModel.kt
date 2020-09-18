/*
 * Created by Force Porquillo on 9/10/20 1:16 PM
 */

package com.force.codes.tracepinas.ui.fragment.viewpager.listview

import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.PagedList.Config.Builder
import com.force.codes.tracepinas.R
import com.force.codes.tracepinas.data.entities.PerCountry
import com.force.codes.tracepinas.data.repository.PerCountryRepository
import com.force.codes.tracepinas.ui.base.BaseViewModel
import com.force.codes.tracepinas.util.ResultWrapper
import com.force.codes.tracepinas.util.constants.PageListConstants.PAGE_MAX_SIZE
import com.force.codes.tracepinas.util.constants.PageListConstants.PAGE_SIZE
import com.force.codes.tracepinas.util.views.Event
import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.NetworkResponse.NetworkError
import com.haroldadmin.cnradapter.NetworkResponse.ServerError
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ListViewModel
@Inject internal constructor(
  private val countryRepository: PerCountryRepository,
) : BaseViewModel() {

  private val _dataLoading = MutableLiveData<Boolean>()
  val dataLoading: LiveData<Boolean> = _dataLoading

  private val _shimmerEffect = MutableLiveData<Boolean>()
  val shimmerEffect: LiveData<Boolean> = _shimmerEffect

  private val _network = MutableLiveData(false)
  val network: LiveData<Boolean> = _network

  private val _showVisibility = MutableLiveData<Boolean>()
  val showVisibility: LiveData<Boolean> = _showVisibility

  private val _forceUpdate = MutableLiveData(false)
  private val _pagedListItems: LiveData<PagedList<PerCountry>> =
    _forceUpdate.switchMap { forceUpdate ->
      if (forceUpdate) {
        _dataLoading.value = true
        _network.value = false
        viewModelScope.launch { filterResultByNetworkUpdate() }
      }
      countryRepository.getFromLocalDataSource(config)
        .distinctUntilChanged().switchMap { filterPagedListResult(it) }
    }

  private fun filterPagedListResult(
    resultFromCache: ResultWrapper<PagedList<PerCountry>>? = null
  ): LiveData<PagedList<PerCountry>> {
    val result = MutableLiveData<PagedList<PerCountry>>()
    resultFromCache?.let {
      if (it is ResultWrapper.Success && it.body.size != 0) {
        result.value = it.body
        false.apply {
          _shimmerEffect.value = this
          _showVisibility.value = this
        }
      } else {
        result.value = null
        true.apply {
          _showVisibility.value = this
          _shimmerEffect.value = this
        }
      }
    }
    return result
  }

  val items: LiveData<PagedList<PerCountry>> = _pagedListItems

  private val _snackBarText = MutableLiveData<Event<Int>>()
  val snackBarText: LiveData<Event<Int>> = _snackBarText

  private fun showSnackBarMessage(@StringRes message: Int) {
    _snackBarText.value = Event(message)
  }

  private suspend fun filterResultByNetworkUpdate(): LiveData<PagedList<PerCountry>> {
    val data = MutableLiveData<PagedList<PerCountry>>()
    when (val response = countryRepository.getFromNetworkSource()) {
      is NetworkResponse.Success -> updateLoadState(false, false, false)
      is NetworkError -> {
        updateLoadState(false, true, false)
        showSnackBarMessage(R.string.failed_update)
      }
      is ServerError -> {
        updateLoadState(false, false, false)
        Timber.e("${response.body}")
        showSnackBarMessage(R.string.server_error)
      }
      is UnknownError -> {
        Timber.e(response)
        updateLoadState(false, false, false)
        showSnackBarMessage(R.string.unknown_error)

        // emit last updated data from cache db.
        filterPagedListResult()
      }
    }
    return data
  }

  private fun updateLoadState(vararg loadState: Boolean?) {
    _dataLoading.value = loadState[0]
    _network.value = loadState[1]
    _showVisibility.value = loadState[2]
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