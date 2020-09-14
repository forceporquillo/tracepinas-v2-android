/*
 * Created by Force Porquillo on 9/4/20 6:45 PM
 */

package com.force.codes.tracepinas.di.module
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.force.codes.tracepinas.di.factory.ViewModelProviderFactory
import com.force.codes.tracepinas.di.scope.ViewModelKey
import com.force.codes.tracepinas.ui.activity.changecountry.ChangeCountryViewModel
import com.force.codes.tracepinas.ui.fragment.viewpager.listview.ListViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
  @Binds
  abstract fun bindViewModelFactory(
    factory: ViewModelProviderFactory
  ): Factory

  /**
   * @link ListViewActivity
   */
  @Binds
  @IntoMap
  @ViewModelKey(ListViewModel::class)
  abstract fun providesNavHostViewModel(
    viewModel: ListViewModel
  ) : ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(ChangeCountryViewModel::class)
  abstract fun providesListViewActivityViewModel(
    activityViewModel: ChangeCountryViewModel
  ) : ViewModel

}