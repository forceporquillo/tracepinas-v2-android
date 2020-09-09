/*
 * Created by Force Porquillo on 9/4/20 6:26 PM
 */

package com.force.codes.tracepinas.di.module

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase.Callback
import com.force.codes.tracepinas.data.source.ApiSource
import com.force.codes.tracepinas.data.source.AppDatabase
import com.force.codes.tracepinas.data.source.ListViewDao
import com.force.codes.tracepinas.util.constants.TIMEOUT_MILLIS
import com.force.codes.tracepinas.util.constants.TWITTER_BEARER_TOKEN
import com.force.codes.tracepinas.util.constants.URL_PATH
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor.Chain
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.HEAD
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Singleton

@Module
object DataBaseModule {
  private var roomCallback = object : Callback() {}

  @Singleton @Provides fun providesLocalDatabase(
    application: Application?,
  ): AppDatabase {
    return Room.databaseBuilder(application!!,
      AppDatabase::class.java,
      "TracePinas.db")
      .fallbackToDestructiveMigration()
      .addCallback(roomCallback)
      .build()
  }

  @Singleton
  @Provides fun providesListViewDao(
    database: AppDatabase
  ): ListViewDao {
    return database.listViewDao()
  }
}

@Module
object NetworkModule {
  @Singleton
  var providesOkHttpClient = Builder()
    .connectTimeout(TIMEOUT_MILLIS.toLong(), MILLISECONDS)
    .readTimeout(TIMEOUT_MILLIS.toLong(), MILLISECONDS)
    .writeTimeout(TIMEOUT_MILLIS.toLong(), MILLISECONDS)
    .addInterceptor(providesLoggingInterceptor())
    .addInterceptor { chain ->
      val makeRequest = chain.request()
        .newBuilder()
        .addHeader("Authorization", "Bearer "
            + TWITTER_BEARER_TOKEN)
        .build()
      chain.proceed(makeRequest)
    }
    .build()

  @Provides fun providesLoggingInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HEADERS
    return interceptor
  }

  @Provides fun providesRetrofitInstance(): Retrofit {
    return Retrofit.Builder()
      .baseUrl(URL_PATH)
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .client(providesOkHttpClient)
      .build()
  }

  @Provides fun providesRemoteApi(
    retrofit: Retrofit,
  ): ApiSource {
    return retrofit.create(ApiSource::class.java)
  }
}