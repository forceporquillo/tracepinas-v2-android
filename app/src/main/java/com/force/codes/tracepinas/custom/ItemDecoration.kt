/*
 * Created by Force Porquillo on 6/14/20 3:25 PM
 * FEU Institute of Technology
 * Copyright (c) 2020.  All rights reserved.
 * Last modified 8/1/20 10:55 PM
 */

package com.force.codes.tracepinas.custom

import android.R.attr
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.recyclerview.widget.RecyclerView.State
import com.force.codes.tracepinas.util.Utils

/**
 * A custom item decoration in [RecyclerView]
 * for adding margin line in each
 * itemView type at runtime.
 *
 * @author Force Porquillo
 */
class ItemDecoration(
  private val context: Context,
  orientation: Int,
  private val margin: Int
) : ItemDecoration() {
  private val drawable: Drawable?
  private var orientation = 0

  private fun setOrientation(
    orientation: Int
  ) {
    require(
        !(orientation != HORIZONTAL_LIST &&
            orientation != VERTICAL_LIST)
    ) { "invalid orientation" }
    this.orientation = orientation
  }

  override fun onDrawOver(
    canvas: Canvas,
    parent: RecyclerView,
    state: State
  ) {
    return if (orientation == VERTICAL_LIST) {
      drawVertical(canvas, parent)
    } else {
      drawHorizontal(canvas, parent)
    }
  }

  private fun drawVertical(
    canvas: Canvas?,
    parent: RecyclerView
  ) {
    val left = parent.paddingLeft
    val right = parent.width - parent.paddingRight
    val childCount = parent.childCount
    for (i in 0 until childCount) {
      val child = parent.getChildAt(i)
      val params = child.layoutParams as LayoutParams
      val top = child.bottom + params.bottomMargin
      val bottom = top + (drawable?.intrinsicHeight ?: 0)
      drawable!!.apply {
        setBounds(
            left + Utils.dpToPx(context, margin, true), top,
            right - Utils.dpToPx(context, margin, true), bottom
        )
        draw(canvas!!)
      }
    }
  }

  private fun drawHorizontal(
    canvas: Canvas?,
    parent: RecyclerView
  ) {
    val top = parent.paddingTop
    val bottom = parent.height - parent.paddingBottom
    val childCount = parent.childCount
    for (i in 0 until childCount) {
      val child = parent.getChildAt(i)
      val params = child.layoutParams as LayoutParams
      val left = child.right + params.rightMargin
      val right = left + (drawable?.intrinsicHeight ?: 0)
      drawable!!.apply {
        setBounds(left, top + Utils.dpToPx(
            context, margin, true
        ),
            right, bottom - Utils.dpToPx(
            context, margin, true
        )
        )
        draw(canvas!!)
      }
    }
  }

  override fun getItemOffsets(
    outRect: Rect,
    view: View,
    parent: RecyclerView,
    state: State
  ) {
    if (orientation == VERTICAL_LIST) {
      outRect[0, 0, 0] = drawable!!.intrinsicHeight
      return
    }
    outRect[0, 0, drawable!!.intrinsicWidth] = 0
  }

  companion object {
    const val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL
    const val VERTICAL_LIST = LinearLayoutManager.VERTICAL
    private val ATTRS = intArrayOf(
        attr.listDivider
    )
  }

  init {
    val typedArray = context.obtainStyledAttributes(
        ATTRS
    )
    drawable = typedArray.getDrawable(0)
    typedArray.recycle()
    setOrientation(orientation)
  }
}