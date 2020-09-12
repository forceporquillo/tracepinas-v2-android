/*
 * Created by Force Porquillo on 9/4/20 6:00 PM
 */

package com.force.codes.tracepinas.data.source

import com.force.codes.tracepinas.data.model.per_country.PerCountry
import com.force.codes.tracepinas.wrapper.ErrorResponse
import com.haroldadmin.cnradapter.NetworkResponse
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiSource {
  @GET suspend fun getAffectedCountries(
    @Url url: String?
  ): List<PerCountry>
}