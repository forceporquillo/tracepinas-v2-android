/*
 * Created by Force Porquillo on 9/2/20 5:38 AM
 */

package com.force.codes.tracepinas.util.events

import android.view.View

interface StackEventListener {
  interface BottomItemListener {
    fun onBottomItemSelected(index: Int)
  }

  interface ListActivityListener {
    fun onStartListViewActivity(view: View?)
  }
}