/*
 * Created by Force Porquillo on 9/5/20 1:27 AM
 */

package com.force.codes.tracepinas.data.model.shared_pref

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SharePrefKey(
  @PrimaryKey
  var id: Int,
  var countryKey: String? = null
)