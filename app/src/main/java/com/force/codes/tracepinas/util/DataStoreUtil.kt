/*
 * Created by Force Porquillo on 9/8/20 1:00 AM
 */

package com.force.codes.tracepinas.util

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
<<<<<<< Updated upstream
=======
import com.force.codes.tracepinas.constants.Constants.DEFAULT
>>>>>>> Stashed changes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

open class DataStoreUtil(
  private val context: Context,
) {

  private val dataStore: DataStore<Preferences> by lazy {
    context.createDataStore(name = "data_pref")
  }

  companion object {
    val COUNTRY_PREF_KEY = preferencesKey<Int>("FIRST_INSTALL_KEY")
    val PRIMARY_PREF = preferencesKey<String>("PRIMARY_COUNTRY_KEY")
  }

  val getStoredVersionCode: Flow<Int> = dataStore.data
      .map { preference ->
        preference[COUNTRY_PREF_KEY] ?: -1
      }

  suspend fun storeVersionCode(
    value: Int
  ) {

    dataStore.edit { preferences ->
      preferences[COUNTRY_PREF_KEY] = value
    }
  }

  val getStoredCountry: Flow<String?> = dataStore.data
      .map { countryKey ->
        countryKey[PRIMARY_PREF]
      }

  suspend fun storePrimaryCountry(
    country: String
  ) {
    dataStore.edit { preferences ->
      preferences[PRIMARY_PREF] = country
    }
  }
}