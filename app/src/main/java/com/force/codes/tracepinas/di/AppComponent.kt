package com.force.codes.tracepinas.di

import com.force.codes.tracepinas.BaseApplication
import com.force.codes.tracepinas.di.module.ActivityBuilderModule
import com.force.codes.tracepinas.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
      AndroidSupportInjectionModule::class,
      AppModule::class,
//    ViewModelModule::class,
//    RepositoryModule::class,
//    DatabaseModule::class,
      ActivityBuilderModule::class,
//    NetworkModule::class,
  ]
)
interface AppComponent {
  fun inject(_application: BaseApplication)

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(_application: BaseApplication): Builder
    fun appModule(appModule: AppModule): Builder
    fun build(): AppComponent
  }
}