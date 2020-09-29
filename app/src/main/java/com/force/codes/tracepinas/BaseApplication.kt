/*
 * Created by Force Porquillo on 9/2/20 4:50 AM
 */

package com.force.codes.tracepinas

import android.app.Application
import android.os.StrictMode
import com.force.codes.tracepinas.di.AppComponent
import com.force.codes.tracepinas.di.module.AppModule
import com.force.codes.tracepinas.util.debug.DebugTreeApp
import com.force.codes.tracepinas.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import leakcanary.AppWatcher
import timber.log.Timber
import javax.inject.Inject

class BaseApplication : Application(), HasAndroidInjector {

  @Inject
  lateinit var injector: DispatchingAndroidInjector<Any>

  val appComponent: AppComponent?
    get() = component

  override fun androidInjector(): AndroidInjector<Any> {
    return injector
  }

  override fun onCreate() {
    /**
     * disabled thread policy
     * when working with Samsung
     * J4 as test DEBUG device
     *
     * if (BuildConfig.DEBUG) {
     *   enabledStrictMode()
     * }
     *
     * permitDiskReads {
     *   super.onCreate()
     * }
     *
     */

    super.onCreate()
    component = DaggerAppComponent.builder()
      .application(this)
      .appModule(AppModule(this))
      .build()
    component.inject(this)

    setDebugInstance()
  }

  private inline fun permitDiskReads(
    func: () -> Any,
  ): Any {
    return if (BuildConfig.DEBUG) {
      val oldThreadPolicy = StrictMode.getThreadPolicy()
      StrictMode.setThreadPolicy(
        StrictMode.ThreadPolicy.Builder(oldThreadPolicy)
          .permitDiskReads().build()
      )
      StrictMode.setThreadPolicy(oldThreadPolicy)
      func()
    } else {
      func()
    }
  }

  companion object {
    private lateinit var component: AppComponent

    private fun setDebugInstance() {
      if (BuildConfig.DEBUG) {
        DebugTreeApp.debug()
        startLeak()
      } else {
        Timber.plant(DebugTreeApp.CrashReportingTree())
      }
    }

    private fun startLeak() {
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
          .penaltyDeath().build()
      )
      StrictMode.setVmPolicy(
        StrictMode.VmPolicy.Builder()
          .detectAll()
          .penaltyLog()
          .penaltyDeath().build()
      )
    }
  }
}