/*
 * Created by Force Porquillo on 9/4/20 6:16 PM
 */

package com.force.codes.tracepinas.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.force.codes.tracepinas.data.model.shared_pref.SharePrefKey

@Dao
interface NavHostDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertCountry(primaryKey: SharePrefKey)
}