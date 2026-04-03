package edu.nd.pmcburne.hello.data

import android.app.Application
import edu.nd.pmcburne.hello.data.PlacemarkApi
import kotlin.getValue
import edu.nd.pmcburne.hello.CampusDatabase

class CampusMapsApplication : Application() {
    val database: CampusDatabase by lazy { CampusDatabase.create(this) }
    val api: PlacemarkApi by lazy { PlacemarkApi.create() }
    val repository: CampusRepository by lazy { CampusRepository(database.dao(), api) }
}