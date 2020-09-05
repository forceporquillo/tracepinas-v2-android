/*
 * Created by Force Porquillo on 9/2/20 4:56 AM
 */

package com.force.codes.tracepinas.di.module

import android.app.Application
import com.force.codes.tracepinas.BaseApplication
import com.force.codes.tracepinas.util.service.ThreadExecutor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

private const val DELAY_QUEUE = 0

@Module
class AppModule constructor(
  private val _application: BaseApplication,
) {

  @Provides @Singleton
  fun providesApplication(): Application {
    return _application
  }

  @Provides
  @Singleton
  fun providesExecutor(): ThreadExecutor {
    return ThreadExecutor(DELAY_QUEUE)
  }
}