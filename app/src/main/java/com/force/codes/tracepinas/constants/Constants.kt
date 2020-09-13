/*
 * Created by Force Porquillo on 9/4/20 6:31 PM
 */

package com.force.codes.tracepinas.constants


object Constants {
  const val TIMEOUT_MILLIS = 1000L
  const val DB_NAME = "TracePinas.db"
  const val START_DATE = "2020-01-22"
  const val TIME_FORMAT = "yyyy-MM-dd"
  const val DEFAULT = "Philippines"
  private const val URL_PATH = "https://corona.lmao.ninja/v2/"
  const val TWITTER_BEARER_TOKEN = "AAAAAAAAAAAAAAAAAAAAAA5SDwEAAAAAnF1q%2BmhNSAysxKDen" +
      "HX4De2H7x0%3DyUJnEvO0oet43pc2mWNGTlNFyF6kXOeprKwF2wx5RRpixV3WBu"

  fun basePath(endpoint: String?): String {
    return URL_PATH.plus(endpoint)
  }
}

object PageListConstants {
  const val PAGE_SIZE = 10
  const val PRE_FETCH_DISTANCE = 20
  const val PAGE_INITIAL_LOAD_SIZE_HINT = 30
  const val PAGE_MAX_SIZE = 215
  const val isEnable = false
}

