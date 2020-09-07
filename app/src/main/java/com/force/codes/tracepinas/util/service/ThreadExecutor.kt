/*
 * Created by Force Porquillo on 9/2/20 4:32 AM
 */

package com.force.codes.tracepinas.util.service

import android.os.Handler
import android.os.Looper
import com.force.codes.tracepinas.util.Utils.threadCount
import java.util.concurrent.Executor
import java.util.concurrent.Executors

open class ThreadExecutor private constructor(
  private val diskIO: Executor,
  private val mainThread: Executor,
  private val computation: Executor,
) {

  constructor(delayQueue: Int) : this(
    Executors.newSingleThreadExecutor(),
    ThreadExecutor(delayQueue),
    Executors.newFixedThreadPool(threadCount)
  )

  fun diskIO(): Executor {
    return diskIO
  }

  fun mainThread(): Executor {
    return mainThread
  }

  fun computationIO(): Executor {
    return computation
  }

  private class ThreadExecutor(
    private val delay: Int,
  ) : Executor {
    private val handler = Handler(Looper.getMainLooper())
    override fun execute(command: Runnable) {
      handler.postDelayed(command, delay.toLong())
    }
  }
}