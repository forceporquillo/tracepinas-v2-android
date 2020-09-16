/*
 * Created by Force Porquillo on 9/3/20 1:08 PM
 */

package com.force.codes.tracepinas.ui.base

import com.force.codes.tracepinas.util.sharedpref.DataStoreUtil
import dagger.android.support.DaggerFragment

abstract class BaseFragment(
  fragmentCountry: Int
) : DaggerFragment(fragmentCountry) {

  val dataStoreUtil: DataStoreUtil by lazy {
    DataStoreUtil(context!!)
  }

}