/*
 * Created by Force Porquillo on 9/9/20 12:49 PM
 */

package com.force.codes.tracepinas.ui.activity.list_view

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.force.codes.tracepinas.data.model.per_country.PerCountry
import com.force.codes.tracepinas.data.source.ChangeCountryDao
import com.force.codes.tracepinas.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChangeCountryViewModel
@Inject internal constructor(
  private val changeCountryDao: ChangeCountryDao
) : BaseViewModel() {

  private val liveData: MutableLiveData<List<PerCountry?>> by lazy {
    MutableLiveData()
  }

  private var changeCountryJob: Job? = null

  @MainThread
  fun orderListViewBy(defaultOrder: Boolean) {
    viewModelScope.launch {
      getListLiveData(defaultOrder)
    }
  }

  private suspend fun getListLiveData(
    defaultOrder: Boolean,
  ): MutableLiveData<List<PerCountry?>> {
    changeCountryDao.queryListViewBy(defaultOrder)
    return liveData
  }

}