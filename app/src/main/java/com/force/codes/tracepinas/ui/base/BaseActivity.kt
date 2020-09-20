/*
 * Created by Force Porquillo on 9/2/20 4:43 AM
 */

package com.force.codes.tracepinas.ui.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.force.codes.tracepinas.BuildConfig.VERSION_CODE
import com.force.codes.tracepinas.R
import com.force.codes.tracepinas.util.sharedpref.DataStoreUtil
import com.force.codes.tracepinas.util.Utils
import com.force.codes.tracepinas.util.Utils.getResColorId
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.launch

abstract class BaseActivity : DaggerAppCompatActivity() {

  protected val dataStoreUtil: DataStoreUtil by lazy {
    DataStoreUtil(this)
  }

  companion object {
    var firstRun: ObservableBoolean = ObservableBoolean()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (Utils.sDKInt >= Build.VERSION_CODES.LOLLIPOP) {
      window.apply {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        getResColorId(this@BaseActivity, R.color.blue)
      }
    } else {
      window.statusBarColor = getResColorId(this@BaseActivity, R.color.white)
    }

    dataStoreUtil.getStoredVersionCode.asLiveData().observe(this, {
      lifecycleScope.launch {
        dataStoreUtil.storeVersionCode(if (it > VERSION_CODE) it else VERSION_CODE)
        firstRun.set(it == -1)
      }
    })
  }
}