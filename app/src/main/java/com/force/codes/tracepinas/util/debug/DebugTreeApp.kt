/*
 * Created by Force Porquillo on 7/2/20 2:49 AM
 * FEU Institute of Technology
 * Copyright (c) 2020.  All rights reserved.
 * Last modified 8/2/20 11:46 AM
 */

package com.force.codes.project.app.app.debug

import android.util.Log
import timber.log.Timber
import java.lang.AssertionError

object DebugTreeApp {
  @JvmStatic fun debug() {
    Timber.plant(Timber.DebugTree())
  }

  class CrashReportingTree : Timber.Tree() {
    override fun log(
      priority: Int,
      tag: String?,
      message: String,
      t: Throwable?
    ) {
      if (priority == Log.VERBOSE
          || priority == Log.DEBUG
      ) {
        return
      }

      CrashLibrary.log(
          priority,
          tag,
          message
      )

      t.let {
        if (priority == Log.ERROR) {
          CrashLibrary.logError(t)
        } else if (priority == Log.WARN) {
          CrashLibrary.logWarning(t)
        }
      }
    }
  }

  class CrashLibrary private constructor() {
    companion object {
      fun log(
        priority: Int,
        tag: String?,
        message: String?
      ){
        Timber.log(priority, message, tag)
      }

      fun logWarning(t: Throwable?){
        Timber.e(t)
      }

      fun logError(t: Throwable?){
        Timber.e(t)
      }
    }

    init {
      throw AssertionError("No instances.")
    }
  }
}