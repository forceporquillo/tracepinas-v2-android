/*
 * Created by Force Porquillo on 9/2/20 6:46 AM
 */

package com.force.codes.tracepinas.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import com.force.codes.tracepinas.R
import com.force.codes.tracepinas.databinding.FragmentStatisticsBinding
import com.force.codes.tracepinas.ui.fragment.viewpager.CurrentCountryFragment
import com.force.codes.tracepinas.ui.fragment.viewpager.GlobalFragment
import com.force.codes.tracepinas.ui.fragment.viewpager.listview.ListViewFragment
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

private const val ALL_VIEW_PAGER = "Overall"
private const val COUNTRY_VIEW_PAGER = "Per Country"
private const val LIST_VIEW_PAGER = "List View"
private const val DEFAULT_INDEX = 1

class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

  private lateinit var binding: FragmentStatisticsBinding
  private var adapter: FragmentPagerItemAdapter? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments.let {
      adapter = FragmentPagerItemAdapter(
        childFragmentManager,
        FragmentPagerItems.with(context!!)
          .apply {
            add(ALL_VIEW_PAGER, GlobalFragment::class.java)
            add(COUNTRY_VIEW_PAGER, CurrentCountryFragment::class.java)
            add(LIST_VIEW_PAGER, ListViewFragment::class.java)
          }
          .create()
      )
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentStatisticsBinding.bind(view).apply {
      viewpager.adapter = adapter
      tabLayout.setViewPager(viewpager)
      statistics = this@StatisticsFragment
      setVariable(BR.statistics, this@StatisticsFragment)
      viewpager.currentItem = DEFAULT_INDEX
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding.unbind()
  }

  companion object {
    @JvmStatic fun newInstance() =
      StatisticsFragment().apply {
        arguments = Bundle().apply {

        }
      }
  }
}