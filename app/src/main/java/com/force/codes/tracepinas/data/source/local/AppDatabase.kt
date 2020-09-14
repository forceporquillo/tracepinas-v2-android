/*
 * Created by Force Porquillo on 9/14/20 2:21 AM
 */

/*
 * Created by Force Porquillo on 9/4/20 5:30 PM
 */

package com.force.codes.tracepinas.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.force.codes.tracepinas.data.entities.DayOne
import com.force.codes.tracepinas.data.entities.Provinces
import com.force.codes.tracepinas.data.entities.PerCountry

@Database(
  entities = [
    DayOne::class,
    PerCountry::class,
    Provinces::class
  ], version = 5, exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
  abstract fun changeCountryDao(): ChangeCountryDao
  abstract fun listViewDao(): ListViewDao

}