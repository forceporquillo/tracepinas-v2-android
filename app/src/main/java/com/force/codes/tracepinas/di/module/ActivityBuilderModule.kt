/*
 * Created by Force Porquillo on 9/2/20 5:14 AM
 */

package com.force.codes.tracepinas.di.module

import com.force.codes.tracepinas.ui.activity.NavHostActivity
import com.force.codes.tracepinas.ui.activity.list_view.ListViewActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {
  @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
  abstract fun bindFragmentContainerActivity(): NavHostActivity

  @ContributesAndroidInjector()
  abstract fun bindFragmentListViewActivity(): ListViewActivity
}