/*
 * Created by Force Porquillo on 9/12/20 4:39 AM
 */

package com.force.codes.tracepinas.wrapper

import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

sealed class ResultWrapper<out T> {
  data class Success<out T>(val value: T) : ResultWrapper<T>()

  data class GenericError(
    val code: Int? = null, val error: ErrorResponse? = null
  ) : ResultWrapper<Nothing>()

  object NetworkError : ResultWrapper<Nothing>()
}

data class ErrorResponse(var errorMessage: String)

suspend fun <T> safeApiCall(
  dispatcher: CoroutineDispatcher,
  apiCall: suspend () -> T
): ResultWrapper<T> {
  return withContext(dispatcher) {
    try {
      ResultWrapper.Success(apiCall.invoke())
    } catch (throwable: Throwable) {
      when (throwable) {
        is IOException -> ResultWrapper.NetworkError
        is HttpException -> {
          val code = throwable.code()
          val errorResponse = convertErrorBody(throwable)
          ResultWrapper.GenericError(code, errorResponse)
        }
        else -> ResultWrapper.GenericError(null, null)
      }
    }
  }
}

private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
  return try {
    throwable.response()?.errorBody()?.source()?.let {
      val moshiAdapter = Moshi.Builder()
        .build().adapter(ErrorResponse::class.java)
      moshiAdapter.fromJson(it)
    }
  } catch (exception: Exception) {
    null
  }
}