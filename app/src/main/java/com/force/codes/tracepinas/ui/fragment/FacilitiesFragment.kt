/*
 * Created by Force Porquillo on 9/2/20 6:48 AM
 */

package com.force.codes.tracepinas.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.force.codes.tracepinas.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FacilitiesFragment : Fragment() {

  private var param1: String? = null
  private var param2: String? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      param1 = it.getString(ARG_PARAM1)
      param2 = it.getString(ARG_PARAM2)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_facilities, container, false)
  }

  companion object {

    @JvmStatic
    fun newInstance(param1: String, param2: String) =
      FacilitiesFragment().apply {
        arguments = Bundle().apply {
          putString(ARG_PARAM1, param1)
          putString(ARG_PARAM2, param2)
        }
      }
  }
}