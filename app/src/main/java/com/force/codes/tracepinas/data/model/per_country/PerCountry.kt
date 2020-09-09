/*
 * Created by Force Porquillo on 9/9/20 12:45 PM
 */

package com.force.codes.tracepinas.data.model.per_country

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity
data class PerCountry(
	@PrimaryKey
	@NotNull
	@SerializedName("country") var country: String,

	@SerializedName("updated") var updated: Int = 0,
	@Embedded @SerializedName("countryInfo") var countryInfo: CountryInfo,
	@SerializedName("cases") var cases: Int = 0,
	@SerializedName("todayCases") var todayCases: Int = 0,
	@SerializedName("deaths") var deaths: Int = 0,
	@SerializedName("todayDeaths") var todayDeaths: Int = 0,
	@SerializedName("recovered") var recovered: Int = 0,
	@SerializedName("todayRecovered") var todayRecovered: Int = 0,
	@SerializedName("active") var active: Int = 0,
	@SerializedName("critical") var critical: Int = 0,
	@SerializedName("casesPerOneMillion") var casesPerOneMillion: Int = 0,
	@SerializedName("deathsPerOneMillion") var deathsPerOneMillion: Int = 0,
	@SerializedName("tests") var tests: Int = 0,
	@SerializedName("testsPerOneMillion") var testsPerOneMillion: Int = 0,
	@SerializedName("population") var population: Int = 0,
	@SerializedName("continent") var continent: String? = null,
	@SerializedName("oneCasePerPeople") var oneCasePerPeople: Int = 0,
	@SerializedName("oneDeathPerPeople") var oneDeathPerPeople: Int = 0,
	@SerializedName("oneTestPerPeople") var oneTestPerPeople: Int = 0,
	@SerializedName("activePerOneMillion") var activePerOneMillion: Double = 0.0,
	@SerializedName("recoveredPerOneMillion") var recoveredPerOneMillion: Double = 0.0,
	@SerializedName("criticalPerOneMillion") var criticalPerOneMillion: Double = 0.0,
)

data class CountryInfo(
	@SerializedName("lat") var lat: Long = 0,
	@SerializedName("long") var long: Long = 0,
	@SerializedName("flag") var flag: String? = null
)