package com.force.codes.tracepinas.di

import com.force.codes.tracepinas.BaseApplication
import com.force.codes.tracepinas.di.module.ActivityBuilderModule
import com.force.codes.tracepinas.di.module.DatabaseModule
import com.force.codes.tracepinas.di.module.AppModule
import com.force.codes.tracepinas.di.module.AppRepositoryModule
import com.force.codes.tracepinas.di.module.NetworkApiModule
import com.force.codes.tracepinas.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    DatabaseModule::class,
    ActivityBuilderModule::class,
    ViewModelModule::class,
    NetworkApiModule::class,
    AppRepositoryModule::class
  ]
)
interface AppComponent {
  fun inject(application: BaseApplication)

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: BaseApplication): Builder
    fun appModule(appModule: AppModule): Builder
    fun build(): AppComponent
  }
}