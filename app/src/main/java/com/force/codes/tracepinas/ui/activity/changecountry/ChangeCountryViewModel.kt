/*
 * Created by Force Porquillo on 9/9/20 12:49 PM
 */

package com.force.codes.tracepinas.ui.activity.changecountry

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.force.codes.tracepinas.data.entities.PerCountry
import com.force.codes.tracepinas.data.source.local.ChangeCountryDao
import com.force.codes.tracepinas.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChangeCountryViewModel
@Inject internal constructor(
  private val changeCountryDao: ChangeCountryDao
) : BaseViewModel() {

  private val _liveData: MutableLiveData<List<PerCountry?>> by lazy {
    MutableLiveData()
  }

  val data: LiveData<List<PerCountry?>> = _liveData

  @MainThread
  fun orderListViewBy(ascending: Boolean) {
    viewModelScope.launch {
      sortListOrderBy(ascending)
    }
  }

  private suspend fun sortListOrderBy(
    ascending: Boolean,
  ): MutableLiveData<List<PerCountry?>> {
    changeCountryDao.queryListViewBy(ascending)
    return _liveData
  }
}