/*
 * Created by Force Porquillo on 9/12/20 3:10 AM
 */

package com.force.codes.tracepinas.di.module

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase.Callback
import androidx.sqlite.db.SupportSQLiteDatabase
import com.force.codes.tracepinas.BuildConfig
import com.force.codes.tracepinas.data.source.local.AppDatabase
import com.force.codes.tracepinas.data.source.local.PerCountryDao
import dagger.Module
import dagger.Provides
import timber.log.Timber
import javax.inject.Singleton

@Module
object DatabaseModule {
  private var roomCallback = object : Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
      super.onCreate(db)
      if (BuildConfig.DEBUG) {
        Timber.d("Creating schema for the first time")
      }
    }
  }

  @Singleton @Provides fun providesLocalDatabase(
    application: Application?,
  ): AppDatabase {
    return Room.inMemoryDatabaseBuilder(application!!, AppDatabase::class.java).build()

//      .databaseBuilder(
//        application!!, AppDatabase::class.java, Constants.DB_NAME)
//      .fallbackToDestructiveMigration()
//      .addCallback(roomCallback)
//      .build()
  }

  @Singleton
  @Provides fun providesPerCountryDao(
    database: AppDatabase
  ): PerCountryDao {
    return database.perCountryDao()
  }

}