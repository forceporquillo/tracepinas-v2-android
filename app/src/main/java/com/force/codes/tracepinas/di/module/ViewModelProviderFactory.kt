/*
 * Created by Force Porquillo on 9/4/20 6:17 PM
 */

package com.force.codes.tracepinas.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

private const val UNKNOWN = "Unknown model class "

@Singleton
open class ViewModelProviderFactory
@Inject constructor(
  private val creators: Map<Class<out ViewModel>,
      @JvmSuppressWildcards Provider<ViewModel>>,
) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    var creator = creators[modelClass]

    creator?.let {
      for (entry in creators) {
        if (modelClass.isAssignableFrom(entry.key)) {
          creator = entry.value
          break
        }
      }
    }

    if (creator == null) {
      throw IllegalArgumentException(UNKNOWN.plus("$modelClass"))
    }

    return try {
      creator!!.get() as T
    } catch (e: Exception) {
      throw RuntimeException(e)
    }
  }
}