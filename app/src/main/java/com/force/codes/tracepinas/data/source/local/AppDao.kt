/*
 * Created by Force Porquillo on 9/14/20 10:05 AM
 */

/*
 * Created by Force Porquillo on 9/4/20 6:16 PM
 */

package com.force.codes.tracepinas.data.source.local

import androidx.paging.DataSource.Factory
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.force.codes.tracepinas.data.entities.PerCountry

@Dao
interface ChangeCountryDao {
  @Query("SELECT * FROM PerCountry ORDER BY CASE WHEN :order = 1 THEN Cases END DESC, CASE WHEN :order = 0 THEN country END")
  suspend fun queryListViewBy(order: Boolean): List<PerCountry?>
}

@Dao
interface ListViewDao {
  @get:Query("SELECT * FROM PerCountry")
  val getFromSource: Factory<Int, PerCountry>?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveToDb(details: List<PerCountry>)

  @Query("DELETE FROM PerCountry")
  suspend fun deleteAllData()
}