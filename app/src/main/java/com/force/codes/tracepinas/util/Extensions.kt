/*
 * Created by Force Porquillo on 9/12/20 3:42 AM
 */

package com.force.codes.tracepinas.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import okio.IOException
import retrofit2.Retrofit

/**
 * add lazy initialization for OkHttpClient
 * Retrofit Builder in NetworkApiModule class.
 */

@PublishedApi
internal inline fun Retrofit.Builder.callFactory(
  crossinline body: (okhttp3.Request) -> okhttp3.Call
) = callFactory(object : okhttp3.Call.Factory {
  override fun newCall(request: okhttp3.Request): okhttp3.Call = body(request)
})

//@ExperimentalCoroutinesApi
//fun <T : Any> Flow<Result<T>>.applyCommonSideEffects() =
//  retryWhen { cause, attempt ->
//    when {
//      (cause is IOException && attempt < Utils.MAX_RETRIES) -> {
//        delay(Utils.getBackoffDelay(attempt))
//        true
//      }
//      else -> {
//        false
//      }
//    }
//  }
//    .onStart { emit(Progress(isLoading = true)) }
//    .onCompletion { emit(Progress(isLoading = false)) }
//
//fun Job?.cancelIfActive() {
//  if (this?.isActive!!) {
//    cancel()
//  }
//}

