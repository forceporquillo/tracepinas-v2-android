/*
 * Created by Force Porquillo on 9/10/20 11:14 PM
 */

/*
 * Created by Force Porquillo on 9/2/20 5:11 AM
 */

package com.force.codes.tracepinas.ui.activity.navigation_host

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.force.codes.tracepinas.custom.BottomBar
import com.force.codes.tracepinas.custom.BottomBarItem
import com.force.codes.tracepinas.custom.BottomItemListener
import com.force.codes.tracepinas.custom.DrawableArray.DRAWABLE_ICONS
import com.force.codes.tracepinas.custom.DrawableArray.FRAGMENT_STACKS
import com.force.codes.tracepinas.custom.DrawableArray.getFragmentIds
import com.force.codes.tracepinas.custom.NavHelper.clearFragmentManagerInstance
import com.force.codes.tracepinas.custom.NavHelper.setDelegateFragment
import com.force.codes.tracepinas.custom.NavHelper.setFragmentManagerInstance
import com.force.codes.tracepinas.databinding.ActivityNavHostBinding
import com.force.codes.tracepinas.ui.base.BaseActivity
import com.force.codes.tracepinas.ui.fragment.StatisticsFragment.Companion.newInstance
import com.jakewharton.processphoenix.ProcessPhoenix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

private const val LAST_NAV_INDEX = "LAST_NAV_INDEX"
private const val LAST_KEY_INDEX = "LAST_KEY_INDEX"
private const val FRAGMENT_STATE = "FRAGMENT_STATE"
private const val START_INDEX = 0

class NavHostActivity : BaseActivity(), BottomItemListener {
  private lateinit var binding: ActivityNavHostBinding
  private lateinit var fragment: Fragment

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityNavHostBinding.inflate(layoutInflater).apply {
      lifecycleOwner = this@NavHostActivity
      setVariable(BR.activity, this@NavHostActivity)
      setContentView(this.root)
    }

    if (ProcessPhoenix.isPhoenixProcess(this).not()) {
      val bottomBar = BottomBar(binding.bottomBar.recyclerView, this, this)
      setBottomBar(bottomBar, savedInstanceState?.getInt(LAST_NAV_INDEX))
      binding.bottomBar.bottomParentContainer.visibility = View.VISIBLE
    }

    setFragmentManagerInstance(supportFragmentManager)

    if (savedInstanceState == null) {
      setDelegateFragment(newInstance()
          .also { fragment = it },
          START_INDEX.also { KEY_INDEX[it] }
      )
    }
  }

  override fun onResume() {
    super.onResume()

    CoroutineScope(Dispatchers.IO).launch {
      delay(1000)
      withContext(Dispatchers.Main) {
        if (firstRun.get()) {
          lifecycleScope.launch {
            Timber.e("Thread Launch")
            dataStoreUtil.storePrimaryCountry("Philippines")
          }
        }
      }
    }
  }

  override fun onBottomItemSelected(
    index: Int,
  ) {
    if (CURRENT_INDEX != index) {
      FRAGMENT_STACKS[index.apply { CURRENT_INDEX = this }]
          .apply {
            changeFragment(this, index)!!
                .also { fragment = it }
          }
    }
  }

  private fun setBottomBar(
    bottomBar: BottomBar,
    index: Int?,
  ) {
    bottomBar.run {
      for (item in addDrawables(applicationContext)) {
        addBottomItem(item)
      }
      setPrimary(index ?: 0)
    }
  }

  override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    savedInstanceState.let {
      try {
        fragment = supportFragmentManager.getFragment(it, FRAGMENT_STATE)!!
      } catch (e: NullPointerException) {
        Timber.e(e)
      } catch (e: IllegalArgumentException) {
        Timber.e(e)
      } finally {
        val intent = Intent(this, NavHostActivity::class.java)
        ProcessPhoenix.triggerRebirth(this, intent)
      }
    }
  }

  override fun onSaveInstanceState(
    outState: Bundle,
  ) {
    super.onSaveInstanceState(outState)
    outState.apply {
      putInt(LAST_NAV_INDEX, CURRENT_INDEX)
      putIntArray(LAST_KEY_INDEX, KEY_INDEX)
      return@apply
    }

    if (fragment.isAdded) {
      supportFragmentManager.putFragment(
          outState,
          FRAGMENT_STATE,
          fragment
      )
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    fixInputMethodLeaks(applicationContext)
    clearFragmentManagerInstance()
    binding.unbind()
  }

  private fun changeFragment(
    fragment: Fragment?,
    index: Int,
  ): Fragment? {
    setDelegateFragment(fragment, index)
    return fragment
  }

  /**
   * prevent leaks when this activity is destroyed
   */
  private fun fixInputMethodLeaks(
    context: Context?,
  ) {
    context?.let {
      return
    } ?: run {
      var inputMethodManager: InputMethodManager? = null

      try {
        inputMethodManager = context?.applicationContext
            ?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
      } catch (t: Throwable) {
        Timber.e(t)
      }

      inputMethodManager ?: {
        val stringArray: Array<String> = arrayOf(
            "mCurRootView", "mServedView", "mNextServedView"
        )
        for (i in 0..KEY_INDEX.size) {
          try {
            val declaredField = inputMethodManager?.javaClass
                ?.getDeclaredField(stringArray[i])
                ?: continue

            if (declaredField.isAccessible.not()) {
              declaredField.isAccessible = true
            }

            val obj = declaredField[inputMethodManager] as? View ?: continue

            if (obj.context === context) {
              declaredField[inputMethodManager] = null
            }
          } catch (_: Throwable) {
          }
        }
      }
    }
  }

  companion object {
    private val KEY_INDEX = IntArray(2)
    private var CURRENT_INDEX = 0

    private fun addDrawables(
      context: Context,
    ): Array<BottomBarItem?> {
      val bottomItems = arrayOfNulls<BottomBarItem>(5)
      for (i in DRAWABLE_ICONS.indices) {
        for (j in DRAWABLE_ICONS[0].indices) {
          if (j == 0) {
            bottomItems[i] = BottomBarItem(
                i, getFragmentIds(context)[i], DRAWABLE_ICONS[i][j],
                DRAWABLE_ICONS[i][START_INDEX + 1]
            )
          }
        }
      }
      return bottomItems
    }
  }
}