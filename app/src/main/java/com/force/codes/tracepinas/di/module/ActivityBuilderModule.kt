/*
 * Created by Force Porquillo on 9/2/20 5:14 AM
 */

package com.force.codes.tracepinas.di.module


import com.force.codes.tracepinas.ui.activity.changecountry.ChangeCountryActivity
import com.force.codes.tracepinas.ui.activity.navigation_host.NavigationHostActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {
  @ContributesAndroidInjector(modules = [FragmentNavHostModule::class])
  abstract fun bindFragmentContainerActivity(): NavigationHostActivity

  @ContributesAndroidInjector
  abstract fun bindFragmentChangeCountryActivity(): ChangeCountryActivity

}