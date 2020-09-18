/*
 * Created by Force Porquillo on 9/17/20 11:37 PM
 */

package com.force.codes.tracepinas.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource.Factory
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.force.codes.tracepinas.data.entities.PerCountry
import com.force.codes.tracepinas.util.ResultWrapper

@Dao
interface PerCountryDao {
  @get:Query("SELECT * FROM PerCountry")
  val getFromSource: Factory<Int, PerCountry>?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveToDb(details: List<PerCountry>)

  @Query("DELETE FROM PerCountry")
  suspend fun deleteAllData()

  @Query("SELECT * FROM PerCountry ORDER BY CASE WHEN :order = 1 THEN Cases END DESC, CASE WHEN :order = 0 THEN country END")
  fun queryListViewBy(order: Boolean): LiveData<List<PerCountry>>
}