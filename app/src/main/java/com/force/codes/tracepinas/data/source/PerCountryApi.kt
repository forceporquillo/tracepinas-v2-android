/*
 * Created by Force Porquillo on 9/4/20 6:00 PM
 */

package com.force.codes.tracepinas.data.source

import com.force.codes.tracepinas.data.entities.PerCountry
import com.force.codes.tracepinas.util.ErrorResponse
import com.force.codes.tracepinas.util.ResultWrapper
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface PerCountryApi {
  @GET suspend fun getPerCountryData(
    @Url url: String?
  ): NetworkResponse<List<PerCountry>, ErrorResponse>
}