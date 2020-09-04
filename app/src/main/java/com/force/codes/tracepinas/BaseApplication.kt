/*
 * Created by Force Porquillo on 9/2/20 4:50 AM
 */

package com.force.codes.tracepinas

import android.app.Application
import com.force.codes.project.app.app.debug.DebugTreeApp
import com.force.codes.tracepinas.di.AppComponent
import com.force.codes.tracepinas.di.DaggerAppComponent
import com.force.codes.tracepinas.di.module.AppModule
import com.force.codes.tracepinas.util.debug.DetectLeak.enabledStrictMode
import com.force.codes.tracepinas.util.debug.DetectLeak.startLeak
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class BaseApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
          .application(this)
          .appModule(AppModule(this))
          .build()
        component.inject(this)
        setDebugInstance()
    }

    val appComponent: AppComponent?
        get() = component

    override fun androidInjector(): AndroidInjector<Any> {
        return injector
    }

    companion object {
        private lateinit var component: AppComponent

        private fun setDebugInstance() {
            if (BuildConfig.DEBUG) {
                DebugTreeApp.debug()
                enabledStrictMode()
                startLeak()
            } else {
                Timber.plant(DebugTreeApp.CrashReportingTree())
            }
        }
    }
}