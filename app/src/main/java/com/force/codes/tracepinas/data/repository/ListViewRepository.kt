/*
 * Created by Force Porquillo on 9/14/20 1:06 AM
 */

/*
 * Created by Force Porquillo on 9/11/20 3:37 PM
 */

package com.force.codes.tracepinas.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PagedList.Config
import com.force.codes.tracepinas.util.constants.Constants.basePath
import com.force.codes.tracepinas.data.entities.PerCountry
import com.force.codes.tracepinas.data.source.ApiSource
import com.force.codes.tracepinas.data.source.local.ListViewDao
import com.force.codes.tracepinas.util.ErrorResponse
import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

interface ListViewRepository {
  fun getFromDataSource(config: Config): LiveData<PagedList<PerCountry>>
  suspend fun updateDataFromRemoteDataSource(): NetworkResponse<List<PerCountry>, ErrorResponse>
}

class ListViewRepositoryImpl @Inject constructor(
  private val apiSource: ApiSource,
  private val listViewDao: ListViewDao,
) : ListViewRepository {

  override fun getFromDataSource(config: Config): LiveData<PagedList<PerCountry>> {
    val factory = listViewDao.getFromSource
    return LivePagedListBuilder(factory!!, config).build()
  }

  override suspend fun updateDataFromRemoteDataSource(): NetworkResponse<List<PerCountry>, ErrorResponse> {
    val networkResponse = executeWithRetry(5) {
      apiSource.getPerCountryData(basePath("countries?sort=cases"))
    }

    when (networkResponse) {
      is NetworkResponse.Success -> {
        listViewDao.apply {
          deleteAllData()
          saveToDb(networkResponse.body)
        }
      }
    }
    return networkResponse
  }
}



