/*
 * Created by Force Porquillo on 9/13/20 2:22 PM
 */

/*
 * Created by Force Porquillo on 9/3/20 12:08 PM
 */

package com.force.codes.tracepinas.ui.fragment.viewpager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.asLiveData
import com.force.codes.tracepinas.R
import com.force.codes.tracepinas.databinding.FragmentCountryBinding
import com.force.codes.tracepinas.ui.activity.changecountry.ChangeCountryActivity
import com.force.codes.tracepinas.ui.base.BaseFragment
import com.force.codes.tracepinas.util.Utils
import com.force.codes.tracepinas.util.Utils.dpToPx
import com.force.codes.tracepinas.util.sharedpref.DataStoreUtil
import timber.log.Timber

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val MARGIN_WIDTH = 10
private const val GRID_COLUMNS = 2

class CurrentCountryFragment : BaseFragment(R.layout.fragment_country), ListActivityListener {
  private var binding: FragmentCountryBinding? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentCountryBinding.bind(view).apply {
      lifecycleOwner = this@CurrentCountryFragment
      listCallback = this@CurrentCountryFragment
      countryFrag = this@CurrentCountryFragment
    }
    setGridBoxAtRuntime(view)
  }

  private fun setGridBoxAtRuntime(
    view: View,
  ) {
    val width = Utils.getDeviceWidth(view.context)
    for (box in boxLayout.indices) {
      boxLayout[box].layoutParams.apply {
        this.width = getBoxWithAtRuntime(
          width, if (box % 2 == 0) MARGIN_WIDTH * GRID_COLUMNS else MARGIN_WIDTH
        )
      }
    }
  }

  private fun getBoxWithAtRuntime(vararg width: Int): Int {
    return width[0] / GRID_COLUMNS - dpToPx(context!!, width[1], true)
  }

  override fun onResume() {
    super.onResume()
    DataStoreUtil(context!!).getStoredCountry.asLiveData()
      .observe(this, { binding!!.spinnerTitle.text = it })
  }

  private val boxLayout: Array<RelativeLayout>
    get() {
      return arrayOf(
        binding!!.containerDeaths, binding!!.containerInfected,
        binding!!.containerRecovered, binding!!.containerTested
      )
    }

  override fun onDestroyView() {
    binding!!.unbind()
    binding = null
    super.onDestroyView()
  }

  companion object {

    @JvmStatic fun newInstance(param1: String, param2: String) =
      CurrentCountryFragment().apply {
        arguments = Bundle().apply {
          putString(ARG_PARAM1, param1)
          putString(ARG_PARAM2, param2)
        }
      }
  }

  override fun onStartListViewActivity() {
    startActivity(Intent(activity, ChangeCountryActivity::class.java))
  }
}

internal interface ListActivityListener {
  fun onStartListViewActivity()
}

