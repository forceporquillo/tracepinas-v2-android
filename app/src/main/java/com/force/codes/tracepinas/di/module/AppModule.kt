/*
 * Created by Force Porquillo on 9/2/20 4:56 AM
 */

package com.force.codes.tracepinas.di.module

import com.force.codes.tracepinas.BaseApplication
import dagger.Module

@Module
class AppModule constructor(
    private val _application: BaseApplication
) {
    val getApplication : BaseApplication
    get() = _application
}