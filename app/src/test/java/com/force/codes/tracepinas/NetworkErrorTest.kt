/*
 * Created by Force Porquillo on 9/14/20 11:58 AM
 */

package com.force.codes.tracepinas

import com.force.codes.tracepinas.util.ErrorResponse
import com.force.codes.tracepinas.util.ResultWrapper
import com.force.codes.tracepinas.util.safeApiCall
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

/**
 * Credits from Douglas Lacovelli
 *
 * Medium => Handling errors with Retrofit and Coroutines in a single place
 *
 */

class NetworkHelperTest {
  @ExperimentalCoroutinesApi
  private val dispatcher = TestCoroutineDispatcher()

  @ExperimentalCoroutinesApi
  @Test
  fun `when lambda returns successfully then it should emit the result as success`() {
    runBlockingTest {
      val lambdaResult = true
      val result = safeApiCall(dispatcher) { lambdaResult }
      assertEquals(ResultWrapper.Success(lambdaResult), result)
    }
  }

  @ExperimentalCoroutinesApi
  @Test
  fun `when lambda throws IOException then it should emit the result as NetworkError`() {
    runBlockingTest {
      val result = safeApiCall(dispatcher) { throw IOException() }
      assertEquals(ResultWrapper.NetworkError, result)
    }
  }

  @ExperimentalCoroutinesApi
  @Test
  fun `when lambda throws HttpException then it should emit the result as GenericError`() {
    val errorBody =
      "{\"errors\": [\"Unexpected parameter\"]}".toResponseBody("application/json".toMediaTypeOrNull())

    runBlockingTest {
      val result = safeApiCall(dispatcher) {
        throw HttpException(Response.error<Any>(422, errorBody))
      }
      assertEquals(
        ResultWrapper.GenericError(422, ErrorResponse("errors")), result)
    }
  }

  @ExperimentalCoroutinesApi
  @Test
  fun `when lambda throws unknown exception then it should emit GenericError`() {
    runBlockingTest {
      val result = safeApiCall(dispatcher) {
        throw IllegalStateException()
      }
      assertEquals(ResultWrapper.GenericError(), result)
    }
  }
}
