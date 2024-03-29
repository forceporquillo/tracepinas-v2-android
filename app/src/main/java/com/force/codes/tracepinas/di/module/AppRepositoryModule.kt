/*
 * Created by Force Porquillo on 9/11/20 11:35 AM
 */

package com.force.codes.tracepinas.di.module

import com.force.codes.tracepinas.data.repository.PerCountryRepository
import com.force.codes.tracepinas.data.repository.PerCountryRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class AppRepositoryModule {
  @Binds
  abstract fun providesListViewRepository(
    repository: PerCountryRepositoryImpl
  ) : PerCountryRepository
}