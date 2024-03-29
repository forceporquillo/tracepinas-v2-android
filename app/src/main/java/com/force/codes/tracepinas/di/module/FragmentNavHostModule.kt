/*
 * Created by Force Porquillo on 9/2/20 5:14 AM
 */

package com.force.codes.tracepinas.di.module

import com.force.codes.tracepinas.ui.fragment.viewpager.CurrentCountryFragment
import com.force.codes.tracepinas.ui.fragment.viewpager.listview.ListViewFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentNavHostModule {
  @ContributesAndroidInjector
  abstract fun contributeCountryFragment() : CurrentCountryFragment

  @ContributesAndroidInjector
  abstract fun contributeListViewFragment() : ListViewFragment
}
