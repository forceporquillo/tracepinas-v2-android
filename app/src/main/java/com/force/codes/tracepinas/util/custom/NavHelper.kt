/*
 * Created by Force Porquillo on 9/2/20 6:39 AM
 */

package com.force.codes.tracepinas.util.custom

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.force.codes.tracepinas.R.anim
import com.force.codes.tracepinas.R.drawable
import com.force.codes.tracepinas.R.id
import com.force.codes.tracepinas.R.string
import com.force.codes.tracepinas.databinding.BottomBarItemBinding
import com.force.codes.tracepinas.ui.fragment.FacilitiesFragment
import com.force.codes.tracepinas.ui.fragment.HelpFragment
import com.force.codes.tracepinas.ui.fragment.MapFragment
import com.force.codes.tracepinas.ui.fragment.NewsFragment
import com.force.codes.tracepinas.ui.fragment.StatisticsFragment
import com.force.codes.tracepinas.util.Utils
import com.force.codes.tracepinas.util.custom.BottomBar.BottomBarItem
import com.force.codes.tracepinas.util.events.StackEventListener

object NavHelper {
  private var SUPPORT_FRAGMENT_MANAGER: FragmentManager? = null
  private var FRAGMENT_INDEX = 0

  fun setFragmentManagerInstance(
    fmInstance: FragmentManager?,
  ) {
    SUPPORT_FRAGMENT_MANAGER = fmInstance
  }

  fun setDelegateFragment(
    fragment: Fragment?,
    itemIndex: Int,
  ) {
    fragment?.let { it ->
      val fragmentTag = it.javaClass.simpleName

      val fragmentTransaction = SUPPORT_FRAGMENT_MANAGER!!.beginTransaction()

      val delegateFragment = arrayOf(
        SUPPORT_FRAGMENT_MANAGER!!.primaryNavigationFragment,
        SUPPORT_FRAGMENT_MANAGER!!.findFragmentByTag(fragmentTag)
      )

      when {
        FRAGMENT_INDEX > itemIndex -> {
          fragmentTransaction.setCustomAnimations(
            anim.enter_from_left,
            anim.exit_to_right
          )
        }
        FRAGMENT_INDEX < itemIndex -> {
          fragmentTransaction.setCustomAnimations(
            anim.enter_from_right,
            anim.exit_to_left
          )
        }
        else -> {
          fragmentTransaction.setCustomAnimations(
            anim.fragment_fade_enter,
            anim.fragment_fade_exit
          )
        }
      }

      FRAGMENT_INDEX = itemIndex

      delegateFragment[0]?.let { fragmentTransaction.hide(it) }

      delegateFragment[1]?.let { f ->
        fragmentTransaction.show(f)
      } ?: run {
        fragmentTransaction.add(id.fragment_container, it.apply {
          delegateFragment[1] = this
        }, fragmentTag
        )
      }

      fragmentTransaction.apply {
        setPrimaryNavigationFragment(delegateFragment[1])
        setReorderingAllowed(true)
        commitAllowingStateLoss()
        return@apply
      }
    }
  }

  /**
   * clear all object instances with
   * attached context to avoid leaks.
   */
  fun clearFragmentManagerInstance() {
    SUPPORT_FRAGMENT_MANAGER = null
  }
}

object DrawableArray {
  fun getFragmentIds(
    context: Context,
  ): Array<String> {
    val resources = context.resources
    return arrayOf(
      resources.getString(string.statistics),
      resources.getString(string.news),
      resources.getString(string.map),
      resources.getString(string.facilities),
      resources.getString(string.help)
    )
  }

  val DRAWABLE_ICONS = arrayOf(
    intArrayOf(drawable.ic_stats, drawable.ic_fill_stats),
    intArrayOf(drawable.ic_news, drawable.ic_fill_news),
    intArrayOf(drawable.ic_map, drawable.ic_fill_map),
    intArrayOf(drawable.ic_hospital, drawable.ic_fill_hospital),
    intArrayOf(drawable.ic_phone, drawable.ic_fill_phone)
  )

  val FRAGMENT_STACKS: List<Fragment>
    get() = ArrayList(listOf(
      StatisticsFragment.newInstance(),
      NewsFragment.newInstance("test1", "test2"),
      MapFragment.newInstance("test1", "test2"),
      FacilitiesFragment.newInstance("test1", "test2"),
      HelpFragment.newInstance("test1", "test2")
    ))
}

private const val ITEM_SIZE = 5

class BottomBar(
  private val recyclerView: RecyclerView,
  private val context: Context,
  private val listener: StackEventListener.BottomItemListener,

  ) {
  private val bottomItems: ArrayList<BottomBarItem> by lazy { ArrayList() }

  fun setPrimary(
    selected: Int,
  ) {
    setBottomAdapter(selected)
  }

  private val computeDeviceWidth: Int
    get() {
      return Utils.dpToPx(context,
        bottomItems.size + 1,
        false
      )
    }

  fun addBottomItem(
    item: BottomBarItem?,
  ) {
    if (bottomItems.size != ITEM_SIZE) {
      item?.let {
        bottomItems.add(it)
      }
    }
  }

  private fun setBottomAdapter(
    selected: Int,
  ) {
    val bottombarAdapter = BottomAdapter(selected,
      computeDeviceWidth, bottomItems, listener
    )
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
    var itemFillIconId: Int,
  )
}

class BottomAdapter(
  private var _selected: Int,
  private val _itemWidth: Int,
  private val bottomItems: ArrayList<BottomBarItem>,
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
      setBottomViews(holder, item.itemTitle, item.itemIconId, item.itemFillIconId, item.itemId)
      setOnClickItem(holder, item.itemId, item.itemIconId, item.itemFillIconId)
    }
  }

  private fun setBottomViews(
    holder: BottomViewHolder,
    title: String,
    vararg items: Int,
  ) {
    holder.apply {
      setIcon(items[0])
      setItemTitle(title)
      resizeItemWidth(_itemWidth)
      selectedStyle(_selected, items[2], items[0], items[1])
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
}

class BottomViewHolder(
  private val itemBinding: BottomBarItemBinding,
) : ViewHolder(itemBinding.root) {

  val container: RelativeLayout
    get() = itemBinding.bottomItemParent

  fun setItemTitle(
    title: String?,
  ) {
    itemBinding.bottomBarTitle.text = title!!
  }

  fun setIcon(
    iconId: Int?,
  ) {
    itemBinding.bottomIcon
      .setImageResource(iconId!!)
  }

  fun selectedStyle(
    select: Int,
    itemId: Int,
    itemDefIcon: Int,
    itemFillIcon: Int,
  ): BottomBarItemBinding {
    val context = itemBinding.root.context
    return if (itemId == select) {
      itemBinding.apply {
        bottomBarTitle.setTextColor(Color.rgb(50, 121, 210))
        bottomIcon.setImageResource(itemFillIcon)
      }
    } else {
      itemBinding.apply {
        bottomBarTitle.setTextColor(Color.rgb(191, 191, 191))
        bottomIcon.setImageResource(itemDefIcon)
      }
    }
  }

  fun resizeItemWidth(
    itemWidth: Int,
  ) {
    itemBinding.bottomParent.apply {
      layoutParams.width = itemWidth
    }
  }
}
