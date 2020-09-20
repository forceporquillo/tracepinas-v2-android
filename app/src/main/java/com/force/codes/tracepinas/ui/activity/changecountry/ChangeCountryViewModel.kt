/*
 * Created by Force Porquillo on 9/9/20 12:49 PM
 */

package com.force.codes.tracepinas.ui.activity.changecountry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.force.codes.tracepinas.data.entities.PerCountry
import com.force.codes.tracepinas.data.repository.PerCountryRepository
import com.force.codes.tracepinas.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ChangeCountryViewModel
@Inject internal constructor(
  private val countryRepository: PerCountryRepository,
) : BaseViewModel() {

  private val _data = MutableLiveData<List<PerCountry>>()
  val data: LiveData<List<PerCountry>> = _data

  private fun emit(ascending: Boolean = true): MutableLiveData<List<PerCountry>> {
    viewModelScope.launch {
      val queryResponse = countryRepository.makeQuery(ascending)
      _data.value = queryResponse.value
    }
    return _data
  }

  fun orderListViewBy(ascending: Boolean): MutableLiveData<List<PerCountry>> {
    return emit(ascending)
  }

  init {
    Timber.e("emit")
    emit(true)
  }
}
