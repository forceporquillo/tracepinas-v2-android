/*
 * Created by Force Porquillo on 9/15/20 2:39 PM
 */

package com.force.codes.tracepinas.util.views

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.force.codes.tracepinas.R.color

fun Fragment.setupRefreshLayout(
  refreshLayout: SwipeRefreshLayout,
  scrollUpChild: View? = null
) {
  refreshLayout.setColorSchemeColors(
    ContextCompat.getColor(requireActivity(), color.blue)
  )
}