/*
 * Created by Force Porquillo on 9/9/20 12:49 PM
 */

package com.force.codes.tracepinas.ui.activity.list_view

import androidx.lifecycle.MutableLiveData
import com.force.codes.tracepinas.data.model.per_country.PerCountry
import com.force.codes.tracepinas.data.source.ListViewDao
import com.force.codes.tracepinas.ui.base.BaseViewModel
import com.force.codes.tracepinas.util.service.ThreadExecutor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ListActivityViewModel
@Inject internal constructor(
  private val listViewDao: ListViewDao,
  private val threadExecutor: ThreadExecutor,
) : BaseViewModel() {

  private val liveData: MutableLiveData<List<PerCountry?>> by lazy {
    MutableLiveData()
  }

  val getLiveData: MutableLiveData<List<PerCountry?>>
    get() {
      getListLiveData(true)
      return liveData
    }

  fun orderListViewBy(defaultOrder: Boolean) {
    getListLiveData(defaultOrder)
  }

  private fun getListLiveData(
    defaultOrder: Boolean,
  ): MutableLiveData<List<PerCountry?>> {
    super.addToUnsubscribed(listViewDao
      .queryListViewBy(defaultOrder)
      .observeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        liveData.value = it
      }) { t: Throwable -> Timber.e(t) }
    )
    return liveData
  }
}