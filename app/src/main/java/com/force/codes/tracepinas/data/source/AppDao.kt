/*
 * Created by Force Porquillo on 9/4/20 6:16 PM
 */

package com.force.codes.tracepinas.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.force.codes.tracepinas.data.model.per_country.PerCountry
import io.reactivex.Flowable

@Dao
interface ListViewDao {
  @Query("SELECT * FROM PerCountry ORDER BY CASE WHEN :cases = 1 THEN Cases END DESC, CASE WHEN :cases = 0 THEN country END")
  fun queryListViewBy(cases: Boolean): Flowable<List<PerCountry?>?>
}