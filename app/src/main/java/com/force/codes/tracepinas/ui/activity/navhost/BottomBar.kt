/*
 * Created by Force Porquillo on 9/2/20 5:37 AM
 */

package com.force.codes.tracepinas.ui.activity.navhost

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.force.codes.tracepinas.ui.events.StackEventListener
import com.force.codes.tracepinas.util.Utils
import java.util.Collections

private const val ITEM_SIZE = 5

class BottomBar(
  private val recyclerView: RecyclerView,
  private val context: Context,
  private val listener: StackEventListener.BottomItemListener

) {
  private val bottomItems: ArrayList<BottomBarItem> by lazy { ArrayList() }

  fun setPrimary(
    selected: Int
  ) {
    setBottomAdapter(selected)
  }

  private val calculateWidth: Int
    get() {
      return Utils.dpToPx(context,
          bottomItems.size + 1,
          false
      )
    }

  fun addBottomItem(
    item: BottomBarItem?
  ) {
    if (bottomItems.size != ITEM_SIZE) {
      item?.let {
        bottomItems.add(it)
      }
    }
  }

  private fun setBottomAdapter(
    selected: Int
  ) {
    val bottombarAdapter = BottomAdapter(selected,
        calculateWidth, bottomItems, listener)

    recyclerView.apply {
      layoutManager = LinearLayoutManager(context,
          LinearLayoutManager.HORIZONTAL,
          false
      )
      adapter = bottombarAdapter
      return@apply
    }
  }

  data class BottomBarItem(
    var itemId: Int,
    var itemTitle: String,
    var itemIconId: Int,
    var itemFillIconId: Int
  )
}