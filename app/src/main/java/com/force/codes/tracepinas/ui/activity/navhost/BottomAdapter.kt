/*
 * Created by Force Porquillo on 9/2/20 5:40 AM
 */

package com.force.codes.tracepinas.ui.activity.navhost

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.force.codes.tracepinas.databinding.BottomBarItemBinding
import com.force.codes.tracepinas.ui.activity.navhost.BottomAdapter.BottomViewHolder
import com.force.codes.tracepinas.ui.events.StackEventListener

class BottomAdapter(
  private var _selected: Int,
  private val _itemWidth: Int,
  private val bottomItems: ArrayList<BottomBar.BottomBarItem>,
  private val listener: StackEventListener.BottomItemListener,
) : Adapter<BottomViewHolder>() {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int,
  ): BottomViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val binding = BottomBarItemBinding.inflate(inflater, parent, false)
    return BottomViewHolder(binding)
  }

  override fun onBindViewHolder(
    holder: BottomViewHolder,
    position: Int,
  ) {
    val item = bottomItems[position]

    if (bottomItems.size != 0) {
      setBottomViews(holder, item)
      setOnClickItem(holder, item.itemId, item.itemIconId, item.itemFillIconId)
    }
  }

  private fun setBottomViews(
    holder: BottomViewHolder,
    item: BottomBar.BottomBarItem,
  ) {
    holder.apply {
      setIcon(item.itemIconId)
      setItemTitle(item.itemTitle)
      resizeItemWidth(_itemWidth)
      selectedStyle(_selected, item.itemId,
          item.itemIconId, item.itemFillIconId
      )
      return@apply
    }
  }

  private fun setOnClickItem(
    holder: BottomViewHolder,
    vararg items: Int,
  ) {
    holder.container.setOnClickListener {
      listener.onBottomItemSelected(items[0].also { _selected = it })
      holder.selectedStyle(_selected, items[0], items[1], items[2])
      notifyDataSetChanged()
    }
  }

  override fun getItemCount(): Int {
    return bottomItems.size
  }

  class BottomViewHolder(
    private val itemBinding: BottomBarItemBinding,
  ) : ViewHolder(itemBinding.root) {
    val container: RelativeLayout
      get() = itemBinding.bottomItemParent

    fun setItemTitle(title: String?) {
      itemBinding.bottomBarTitle.text = title!!
    }

    fun setIcon(iconId: Int?) {
      itemBinding.bottomIcon
          .setImageResource(
              iconId!!
          )
    }

    fun selectedStyle(
      select: Int,
      itemId: Int,
      itemDefIcon: Int,
      itemFillIcon: Int,
    ) {
      return if (itemId == select) {
        itemBinding.bottomBarTitle
            .setTextColor(
                Color.rgb(50, 121, 210)
            )
        itemBinding.bottomIcon
            .setImageResource(
                itemFillIcon
            )
      } else {
        itemBinding.bottomBarTitle
            .setTextColor(
                Color.rgb(191, 191, 191)
            )
        itemBinding.bottomIcon
            .setImageResource(
                itemDefIcon
            )
      }
    }

    fun resizeItemWidth(
      itemWidth: Int,
    ) {
      itemBinding.bottomParent.layoutParams.width = itemWidth
    }
  }
}