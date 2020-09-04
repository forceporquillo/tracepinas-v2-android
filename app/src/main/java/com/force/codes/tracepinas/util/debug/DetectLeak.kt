/*
 * Created by Force Porquillo on 9/2/20 4:43 AM
 */

package com.force.codes.tracepinas.util.debug

import android.os.StrictMode
import leakcanary.AppWatcher

object DetectLeak{
  fun startLeak() {
    AppWatcher.config.watchActivities
    AppWatcher.config.watchFragments
    AppWatcher.config.watchFragmentViews
    AppWatcher.config.watchViewModels

    val objectWatcher =
      AppWatcher.objectWatcher
    objectWatcher.retainedObjectCount
  }

  fun enabledStrictMode() {
    StrictMode.setThreadPolicy(
        StrictMode.ThreadPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .penaltyDeath()
            .build()
    )
  }
}