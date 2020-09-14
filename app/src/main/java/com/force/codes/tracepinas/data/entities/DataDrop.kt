/*
 * Created by Force Porquillo on 9/14/20 1:05 AM
 */

/*
 * Created by Force Porquillo on 9/4/20 5:25 PM
 */

package com.force.codes.tracepinas.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * DOH data drop from csv
 */

@Entity
data class DataTimeline(
  @PrimaryKey
  val id: Int = 1,

  @Embedded
  @field:SerializedName("data")
  val data: List<DayOne>
)

@Entity
data class DayOne(
  @PrimaryKey
  val uid: Int,

  @field:SerializedName("date")
  val date: String? = null,

  @field:SerializedName("recovered")
  val recovered: Int = 0,

  @field:SerializedName("cases")
  val cases: Int = 0,

  @field:SerializedName("died")
  val died: Int = 0,
)

data class Regions(
  @field:SerializedName("data")
  val data: List<Provinces?>? = null,
)

@Entity
data class Provinces(
  @PrimaryKey
  @field:SerializedName("region")
  val region: String,

  @field:SerializedName("recovered")
  val recovered: Int = 0,

  @field:SerializedName("cases")
  val cases: Int = 0,

  @field:SerializedName("deaths")
  val deaths: Int = 0,
)

