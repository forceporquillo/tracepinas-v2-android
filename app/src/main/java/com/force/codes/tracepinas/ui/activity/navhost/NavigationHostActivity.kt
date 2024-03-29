/*
 * Created by Force Porquillo on 9/15/20 12:46 AM
 */

package com.force.codes.tracepinas.ui.activity.navhost

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

import com.force.codes.tracepinas.ui.activity.navhost.DrawableArray.DRAWABLE_ICONS
import com.force.codes.tracepinas.ui.activity.navhost.DrawableArray.FRAGMENT_STACKS
import com.force.codes.tracepinas.ui.activity.navhost.DrawableArray.getFragmentIds
import com.force.codes.tracepinas.ui.activity.navhost.NavHelper.setDelegateFragment
import com.force.codes.tracepinas.ui.activity.navhost.NavHelper.setFragmentManagerInstance
import com.force.codes.tracepinas.databinding.ActivityNavHostBinding
import com.force.codes.tracepinas.ui.activity.navhost.NavHelper.clearFragmentInstance
import com.force.codes.tracepinas.ui.base.BaseActivity
import com.force.codes.tracepinas.ui.fragment.StatisticsFragment.Companion.newInstance

import com.jakewharton.processphoenix.ProcessPhoenix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.IndexOutOfBoundsException
import java.lang.NumberFormatException

private const val LAST_NAV_INDEX = "LAST_NAV_INDEX"
private const val LAST_KEY_INDEX = "LAST_KEY_INDEX"
private const val FRAGMENT_STATE = "FRAGMENT_STATE"
private const val START_INDEX = 0

class NavigationHostActivity : BaseActivity(), BottomItemListener {

  private lateinit var binding: ActivityNavHostBinding

  private lateinit var fragment: Fragment

  private lateinit var _bottomBar: BottomBar

  companion object {
    private val KEY_INDEX = IntArray(2)
    private var CURRENT_INDEX = 0

    private fun addDrawables(
      context: Context,
    ): Array<BottomBarItem?> {
      val bottomItems = arrayOfNulls<BottomBarItem>(5)
      for (i in DRAWABLE_ICONS.indices) {
        for (j in 0..i) {
          if (j == 0) {
            bottomItems[i] = BottomBarItem(
              i, getFragmentIds(context)[i],
              DRAWABLE_ICONS[i][j], DRAWABLE_ICONS[i][START_INDEX + 1]
            )
          }
        }
      }
      return bottomItems
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityNavHostBinding.inflate(layoutInflater)
      .apply {
        lifecycleOwner = this@NavigationHostActivity
        setVariable(BR.activity, this@NavigationHostActivity)
        setContentView(root)
        executePendingBindings()
      }

    if (!ProcessPhoenix.isPhoenixProcess(this)) {
      binding.bottomBar.apply {
        _bottomBar = BottomBar(
          recyclerView,
          this@NavigationHostActivity,
          this@NavigationHostActivity
        )
        setBottomBar(savedInstanceState?.getInt(LAST_NAV_INDEX), _bottomBar)
        bottomParentContainer.visibility = View.VISIBLE
      }
    }

    setFragmentManagerInstance(supportFragmentManager)

    if (savedInstanceState == null) {
      setDelegateFragment(newInstance()
        .also { fragment = it },
        START_INDEX.also { KEY_INDEX[it] }
      )
    }

    // triggers when app is first installed in the device
    if (firstRun.get()) {
      CoroutineScope(Dispatchers.IO).launch {
        delay(1000)
        withContext(Dispatchers.Main) {
          lifecycleScope.launch {
            // store default country
            dataStoreUtil.storePrimaryCountry("Philippines")
          }
        }
      }
    }
  }

  override fun onBottomItemSelected(index: Int) {
    if (CURRENT_INDEX != index) {
      try {
        FRAGMENT_STACKS[index.apply { CURRENT_INDEX = this }]
          .apply {
            changeFragment(this, index)!!
              .also { fragment = it }
          }
      } catch (_: IndexOutOfBoundsException) {
        forceRestart()
      }
    }
  }

  private fun setBottomBar(index: Int?, bottomBar: BottomBar) {
    bottomBar.apply {
      for (item in addDrawables(applicationContext)) {
        addBottomItem(item)
      }
      setPrimary(index ?: 0)
    }
  }

  /**
   * Save last bottom navigation index and retain instance
   * of a fragment. Otherwise, restart app if throws exception.
   */
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
        forceRestart()
      }
    }
  }

  private fun forceRestart() {
    val intent = Intent(this, NavigationHostActivity::class.java)
    ProcessPhoenix.triggerRebirth(this, intent)
  }

  override fun onSaveInstanceState(outState: Bundle) {
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
    clearFragmentInstance
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
}

