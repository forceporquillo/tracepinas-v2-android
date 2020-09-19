/*
 * Created by Force Porquillo on 9/14/20 10:44 AM
 */

package com.force.codes.tracepinas.util

import com.force.codes.tracepinas.util.Resource.Status.ERROR
import com.force.codes.tracepinas.util.Resource.Status.LOADING
import com.force.codes.tracepinas.util.Resource.Status.SUCCESS
import com.force.codes.tracepinas.util.ResultWrapper.GenericError
import com.force.codes.tracepinas.util.ResultWrapper.NetworkError
import com.force.codes.tracepinas.util.ResultWrapper.Success
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

sealed class ResultWrapper<out T : Any> {
  data class Success<out T : Any>(
    val body: T
  ) : ResultWrapper<T>()

  data class GenericError(
    val code: Int? = null,
    val error: ErrorResponse? = null
  ) : ResultWrapper<Nothing>()

  object NetworkError : ResultWrapper<Nothing>()
}

data class ErrorResponse(var errorMessage: String)

suspend fun <T : Any> safeApiCall(
  dispatcher: CoroutineDispatcher,
  apiCall: suspend () -> T
): ResultWrapper<T> {
  return withContext(dispatcher) {
    try {
      Success(apiCall.invoke())
    } catch (throwable: Throwable) {
      throwable.let {
        when (it) {
          is IOException -> NetworkError
          is HttpException -> {
            val code = it.code()
            val errorResponse = convertErrorBody(it)
            GenericError(code, errorResponse)
          }
          else -> GenericError(null, null)
        }
      }
    }
  }
}

private fun convertErrorBody(
  throwable: HttpException
): ErrorResponse? {
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

data class Resource<out T>(
  val status: Status,
  val data: T?,
  val message: String?
) {

  enum class Status {
    SUCCESS,
    ERROR,
    LOADING
  }

  companion object {
    fun <T> success(data: T): Resource<T> {
      return Resource(SUCCESS, data, null)
    }

    fun <T> error(message: String, data: T? = null): Resource<T> {
      return Resource(ERROR, data, message)
    }

    fun <T> loading(data: T? = null): Resource<T> {
      return Resource(LOADING, data, null)
    }
  }
}

data class FetchResult(var isSuccess: Boolean)

