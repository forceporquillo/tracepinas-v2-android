/*
 * Created by Force Porquillo on 9/2/20 5:14 AM
 */

package com.force.codes.tracepinas.di.module

import com.force.codes.tracepinas.ui.activity.NavHostActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {
    @ContributesAndroidInjector
    abstract fun bindFragmentContainerActivity(): NavHostActivity?
}