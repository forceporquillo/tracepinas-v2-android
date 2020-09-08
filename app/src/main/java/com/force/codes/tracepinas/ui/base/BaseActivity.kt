/*
 * Created by Force Porquillo on 9/2/20 4:43 AM
 */

package com.force.codes.tracepinas.ui.base

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.force.codes.tracepinas.BuildConfig.VERSION_CODE
import com.force.codes.tracepinas.R
import com.force.codes.tracepinas.util.DataStoreUtil
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseActivity : DaggerAppCompatActivity() {

  protected val dataStoreUtil: DataStoreUtil by lazy {
    DataStoreUtil(this)
  }

  companion object {
    private var isFirstRun = false
    private const val NOT_EXIST = -1

    val checkIfFirstRun
      get() = isFirstRun
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    window.statusBarColor.apply {
      ContextCompat.getColor(this@BaseActivity, R.color.white)
    }

    dataStoreUtil.getStoredVersionCode.asLiveData().observe(this, { versionCode ->
      when {
        VERSION_CODE == versionCode -> {
          return@observe
        }
        versionCode == NOT_EXIST -> {
          isFirstRun = true
        }
      }

      val storeKey = if (versionCode > VERSION_CODE) versionCode else VERSION_CODE
      lifecycleScope.launch { dataStoreUtil.storeVersionCode(storeKey) }
    })
  }
}