/*
 * Created by Force Porquillo on 9/14/20 1:06 AM
 */

package com.force.codes.tracepinas.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PagedList.Config
import com.force.codes.tracepinas.data.entities.PerCountry
import com.force.codes.tracepinas.data.source.ApiSource
import com.force.codes.tracepinas.data.source.local.PerCountryDao
import com.force.codes.tracepinas.util.ErrorResponse
import com.force.codes.tracepinas.util.ResultWrapper
import com.force.codes.tracepinas.util.ResultWrapper.Success
import com.force.codes.tracepinas.util.constants.ApiConstants
import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import javax.inject.Inject

interface PerCountryRepository {
  fun getFromLocalDataSource(config: Config): LiveData<ResultWrapper<PagedList<PerCountry>>>
  suspend fun getFromNetworkSource() : NetworkResponse<List<PerCountry>, ErrorResponse>
  fun makeQuery(ascending: Boolean): LiveData<ResultWrapper<List<PerCountry>>>
}

class PerCountryRepositoryImpl @Inject constructor(
  private val apiSource: ApiSource,
  private val perCountryDao: PerCountryDao,
) : PerCountryRepository {

  override fun getFromLocalDataSource(config: Config): LiveData<ResultWrapper<PagedList<PerCountry>>> {
    return LivePagedListBuilder(perCountryDao.getFromSource!!, config).build().map {
      Success(it)
    }
  }

  override suspend fun getFromNetworkSource() : NetworkResponse<List<PerCountry>, ErrorResponse> {
    val networkResponse = executeWithRetry(5) {
      apiSource.getPerCountryData(ApiConstants.basePath("countries?sort=cases"))
    }
    when (networkResponse) {
      is NetworkResponse.Success -> {
        perCountryDao
          .apply {
            deleteAllData()
            saveToDb(networkResponse.body)
          }
      }
    }

    return networkResponse
  }

  override fun makeQuery(ascending: Boolean): LiveData<ResultWrapper<List<PerCountry>>> {
    return perCountryDao.queryListViewBy(ascending).map {
      Success(it)
    }
  }
}



