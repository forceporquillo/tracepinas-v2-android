/*
 * Created by Force Porquillo on 9/4/20 6:10 PM
 */

package com.force.codes.tracepinas.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel protected constructor() : ViewModel() {
  private val compositeDisposable: CompositeDisposable = CompositeDisposable()

  fun addToUnsubscribed(disposable: Disposable?) {
    compositeDisposable.add(disposable!!)
  }

  override fun onCleared() {
    super.onCleared()
    if (!compositeDisposable.isDisposed) {
      compositeDisposable.clear()
      compositeDisposable.dispose()
    }
  }
}