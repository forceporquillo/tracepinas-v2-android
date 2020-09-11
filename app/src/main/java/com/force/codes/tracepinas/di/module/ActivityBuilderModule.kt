/*
 * Created by Force Porquillo on 9/2/20 5:14 AM
 */

package com.force.codes.tracepinas.di.module

<<<<<<< Updated upstream
import com.force.codes.tracepinas.ui.activity.NavHostActivity
=======
import com.force.codes.tracepinas.ui.activity.navigation_host.NavHostActivity
import com.force.codes.tracepinas.ui.activity.list_view.ChangeCountryActivity
>>>>>>> Stashed changes
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {
  @ContributesAndroidInjector(modules = [FragmentNavHostModule::class])
  abstract fun bindFragmentContainerActivity(): NavHostActivity
<<<<<<< Updated upstream
=======

  @ContributesAndroidInjector()
  abstract fun bindFragmentListViewActivity(): ChangeCountryActivity
>>>>>>> Stashed changes
}