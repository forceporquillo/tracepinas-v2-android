/*
 * Created by Force Porquillo on 9/15/20 12:46 AM
 */

package com.force.codes.tracepinas.ui.activity.navhost

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.AsyncListDiffer
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
import com.force.codes.tracepinas.util.adapter.Binder
import com.force.codes.tracepinas.util.adapter.DiffUtilComparator
import timber.log.Timber

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
            anim.enter_from_left, anim.exit_to_right
          )
        }
        FRAGMENT_INDEX < itemIndex -> {
          fragmentTransaction.setCustomAnimations(
            anim.enter_from_right, anim.exit_to_left
          )
        }
        else -> {
          fragmentTransaction.setCustomAnimations(
            anim.fragment_fade_enter, anim.fragment_fade_exit
          )
        }
      }

      FRAGMENT_INDEX = itemIndex

      delegateFragment[0]?.let { fragmentTransaction.hide(it) }

      delegateFragment[1]?.let { f ->
        fragmentTransaction.show(f)
      } ?: run {
        fragmentTransaction.add(
          id.fragment_container,
          it.apply {
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
  val clearFragmentInstance: () -> Unit
    get() = {
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

  @DrawableRes val DRAWABLE_ICONS = arrayOf(
    intArrayOf(drawable.ic_stats, drawable.ic_fill_stats),
    intArrayOf(drawable.ic_news, drawable.ic_fill_news),
    intArrayOf(drawable.ic_map, drawable.ic_fill_map),
    intArrayOf(drawable.ic_hospital, drawable.ic_fill_hospital),
    intArrayOf(drawable.ic_phone, drawable.ic_fill_phone)
  )

  val FRAGMENT_STACKS: List<Fragment>
    get() = ArrayList(
      listOf(
        StatisticsFragment(),
        NewsFragment(),
        MapFragment(),
        FacilitiesFragment(),
        HelpFragment()
      )
    )
}

interface BottomItemListener {
  fun onBottomItemSelected(index: Int)
}

class BottomBar(
  private val recyclerView: RecyclerView,
  private val context: Context,
  private val listener: BottomItemListener,
) {

  private val bottomItems = ArrayList<BottomBarItem>()

  fun setPrimary(selected: Int) {
    setBottomAdapter(selected)
  }

  private val computeDeviceWidth: Int
    get() {
      return Utils.dpToPx(
        context,
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
    val bottombarAdapter = BottomAdapter(
      selected,
      computeDeviceWidth, bottomItems, listener
    )
    recyclerView.apply {
      layoutManager = LinearLayoutManager(
        context,
        LinearLayoutManager.HORIZONTAL,
        false
      )
      adapter = bottombarAdapter
      return@apply
    }
  }
}

data class BottomBarItem(
  var itemId: Int,
  var itemTitle: String,
  @DrawableRes var itemIconId: Int,
  @DrawableRes var itemFillIconId: Int,
)

private const val ITEM_SIZE = 5

class BottomAdapter(
  private var selected: Int,
  private val itemWidth: Int,
  bottomItems: ArrayList<BottomBarItem>,
  private val listener: BottomItemListener,
) : Adapter<BottomViewHolder>() {

  private val asyncListDiffer = AsyncListDiffer(this, DiffUtilComparator<BottomBarItem>())

  init {
    asyncListDiffer.submitList(bottomItems)
  }

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
    val item = asyncListDiffer.currentList[position]
    holder.apply {
      (this as Binder<*>).bind(item)
      scaleItemWidth(itemWidth)
      selectedStyle(selected)
    }
    setOnClickItem(holder, position)
  }

  private fun setOnClickItem(
    holder: BottomViewHolder,
    items: Int,
  ) {
    holder.container.setOnClickListener {
      items.apply {
        listener.onBottomItemSelected(this)
        holder.selectedStyle(this)
        selected = this
      }
      notifyDataSetChanged()
    }
  }

  override fun getItemCount(): Int {
    return asyncListDiffer.currentList.size
  }
}

class BottomViewHolder(
  private val itemBinding: BottomBarItemBinding,
) : ViewHolder(itemBinding.root), Binder<BottomBarItem> {

  val container: RelativeLayout
    get() = itemBinding.bottomItemParent

  private lateinit var bottomItems: BottomBarItem

  override fun bind(data: Any?) {
    bottomItems = (data as BottomBarItem).apply {
      itemBinding.apply {
        viewholder = this@BottomViewHolder
        bottomItem = data
        setVariable(BR.bottomItem, data)
        bottomIcon.setImageResource(itemIconId)
        executePendingBindings()
      }
    }
  }

  fun selectedStyle(selectedItem: Int) {
    bottomItems.apply {
      if (itemId == selectedItem) {
        itemBinding.apply {
          bottomBarTitle.setTextColor(Color.rgb(50, 121, 210))
          bottomIcon.setImageResource(itemFillIconId)
        }
      } else {
        itemBinding.apply {
          bottomBarTitle.setTextColor(Color.rgb(191, 191, 191))
          bottomIcon.setImageResource(itemIconId)
        }
      }
    }
  }

  fun scaleItemWidth(itemWidth: Int) {
    itemBinding.bottomParent.apply {
      layoutParams.width = itemWidth
    }
  }
}
