/*
 * Created by Force Porquillo on 9/15/20 2:39 PM
 */

package com.force.codes.tracepinas.util.views

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.force.codes.tracepinas.R.color
import com.google.android.material.snackbar.Snackbar

fun Fragment.setupRefreshLayout(
  refreshLayout: SwipeRefreshLayout,
  scrollUpChild: View? = null
) {
  refreshLayout.setColorSchemeColors(
    ContextCompat.getColor(requireActivity(), color.blue)
  )
}

fun View.setupSnackBar(
  lifecycleOwner: LifecycleOwner,
  snackBarEvent: LiveData<Event<Int>>,
  timeLength: Int
) {
  snackBarEvent.observe(lifecycleOwner,  { event ->
    event.getContentIfNotHandled()?.let {
      showSnackBar(context.getString(it), timeLength)
    }
  })
}

fun View.showSnackBar(
  snackBarText: String,
  timeLength: Int
) {
  Snackbar.make(this, snackBarText, timeLength).run {
    addCallback(object: Snackbar.Callback() {
      override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
        super.onDismissed(transientBottomBar, event)
      }

      override fun onShown(sb: Snackbar?) {
        super.onShown(sb)
      }
    })
    show()
  }
}

open class Event<out T>(private val content: T) {
  @Suppress("MemberVisibilityCanBePrivate")
  var hasBeenHandle = false
    private set

  fun getContentIfNotHandled() : T? {
    return if (hasBeenHandle) {
      null
    } else {
      hasBeenHandle = true
      content
    }
  }
}

class EventObserver<T>(
  private val onEventUnHandleContent: (T) -> Unit
) : Observer<Event<T>> {
  override fun onChanged(t: Event<T>?) {
    t?.getContentIfNotHandled()?.let {
      onEventUnHandleContent(it)
    }
  }
}