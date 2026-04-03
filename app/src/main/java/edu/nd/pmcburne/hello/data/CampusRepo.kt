package edu.nd.pmcburne.hello.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import edu.nd.pmcburne.hello.CampusDao

class CampusRepository(
    private val dao: CampusDao,
    private val api: PlacemarkApi,
) {
    fun getAllTags(): Flow<List<String>> = dao.getAllDistinctTags()

    fun getLocationsForTag(tag: String): Flow<List<CampusLocation>> =
        dao.getLocationsForTag(tag).map { locations ->
            locations.map { it.toCampusLocation() }
        }

    suspend fun syncFromApi() {
        val placemarks = api.getPlacemarks()
        val locationEntities = placemarks.map { it.toEntity() }
        val tagEntities = placemarks.flatMap { it.toTagEntities() }
        dao.replaceLocationsAndTags(
            locations = locationEntities,
            tags = tagEntities,
        )
    }
}
