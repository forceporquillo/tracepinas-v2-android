/*
 * Created by Force Porquillo on 9/9/20 12:49 PM
 */

package com.force.codes.tracepinas.ui.activity.changecountry

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.force.codes.tracepinas.data.entities.PerCountry
import com.force.codes.tracepinas.data.repository.PerCountryRepository
import com.force.codes.tracepinas.ui.base.BaseViewModel
import com.force.codes.tracepinas.util.ResultWrapper
import com.force.codes.tracepinas.util.ResultWrapper.Success
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChangeCountryViewModel
@Inject internal constructor(
  private val countryRepository: PerCountryRepository,
) : BaseViewModel() {

  private val _data = MutableLiveData<List<PerCountry>>()
  val data: LiveData<List<PerCountry>> = _data

  private fun emit(ascending: Boolean = true): MutableLiveData<List<PerCountry>> {
    viewModelScope.launch {
      val queryResponse = countryRepository.makeQuery(ascending).value
      if (queryResponse is Success) {
        viewModelScope.launch {
         _data.value = queryResponse.body
        }
      }
    }
    return _data
  }

  @MainThread
  fun orderListViewBy(ascending: Boolean): MutableLiveData<List<PerCountry>> {
    return emit(ascending)
  }

  init {
    emit(true)
  }
}