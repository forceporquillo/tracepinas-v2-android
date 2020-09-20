/*
 * Created by Force Porquillo on 9/4/20 6:26 PM
 */

package com.force.codes.tracepinas.di.module

import com.force.codes.tracepinas.BuildConfig
import com.force.codes.tracepinas.util.constants.Constants.TIMEOUT_MILLIS
import com.force.codes.tracepinas.data.source.ApiSource
import com.force.codes.tracepinas.util.constants.ApiConstants.TWITTER_BEARER_TOKEN
import com.force.codes.tracepinas.util.constants.ApiConstants.basePath
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Singleton

@Module
object NetworkApiModule {

  @Provides
  @Singleton
  fun provideOkHttpClient(): OkHttpClient {
    return Builder().apply {
      connectTimeout(TIMEOUT_MILLIS, MILLISECONDS)
      readTimeout(TIMEOUT_MILLIS, MILLISECONDS)
      writeTimeout(TIMEOUT_MILLIS, MILLISECONDS)

      if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor().apply {
          level = BODY
          addInterceptor(this)
        }
      }

      addInterceptor { chain ->
        val makeRequest = chain.request()
          .newBuilder()
          .addHeader("Authorization", "Bearer $TWITTER_BEARER_TOKEN")
          .build()
        chain.proceed(makeRequest)
      }
    }
      .build()
  }

  @Provides
  @Singleton
  fun provideRetrofitInstance(
  ): Retrofit {
    return Retrofit.Builder()
      .baseUrl(basePath(""))
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(NetworkResponseAdapterFactory())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .client(provideOkHttpClient())
      .build()
  }

  @Provides fun provideApi(
    retrofit: Retrofit,
  ): ApiSource {
    return retrofit.create(ApiSource::class.java)
  }
}