/*
 * Created by Force Porquillo on 9/4/20 6:45 PM
 */

package com.force.codes.tracepinas.di.module
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.force.codes.tracepinas.di.scope.ViewModelKey
import com.force.codes.tracepinas.ui.viewmodel.CountryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
  @Binds
  abstract fun bindViewModelFactory(
    factory: ViewModelProviderFactory
  ): Factory

  @Binds
  @IntoMap
  @ViewModelKey(CountryViewModel::class)
  abstract fun providesCountryViewModel(
    viewModel: CountryViewModel
  ) : ViewModel
}