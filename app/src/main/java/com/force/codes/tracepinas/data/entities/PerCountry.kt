/*
 * Created by Force Porquillo on 9/14/20 1:05 AM
 */

/*
 * Created by Force Porquillo on 9/9/20 12:45 PM
 */

package com.force.codes.tracepinas.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity
data class PerCountry(
	@PrimaryKey @NotNull
	var country: String,
	var updated: Long = 0L,
	var cases: Int = 0,
	var todayCases: Int = 0,
	var deaths: Int = 0,
	var todayDeaths: Int = 0,
	var recovered: Int = 0,
	var todayRecovered: Int = 0,
	var active: Int = 0,
	var critical: Int = 0,
	var casesPerOneMillion: Int = 0,
	var deathsPerOneMillion: Double = 0.0,
	var tests: Int = 0,
	var testsPerOneMillion: Int = 0,
	var population: Int = 0,
	var continent: String? = null,
	var oneCasePerPeople: Int = 0,
	var oneDeathPerPeople: Int = 0,
	var oneTestPerPeople: Int = 0,
	var activePerOneMillion: Double = 0.0,
	var recoveredPerOneMillion: Double = 0.0,
	var criticalPerOneMillion: Double = 0.0,

	@Embedded var countryInfo: CountryInfo,
)

data class CountryInfo(
	var lat: Double? = 0.0,
	var long: Double? = 0.0,
	var flag: String? = null
)