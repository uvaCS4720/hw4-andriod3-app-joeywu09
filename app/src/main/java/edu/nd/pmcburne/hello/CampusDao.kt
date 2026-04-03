package edu.nd.pmcburne.hello

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import edu.nd.pmcburne.hello.data.LocationEntity
import edu.nd.pmcburne.hello.data.LocationTagEntity

@Dao
interface CampusDao {
    @Upsert
    suspend fun upsertLocations(locations: List<LocationEntity>)

    @Upsert
    suspend fun upsertLocationTags(tags: List<LocationTagEntity>)

    @Query("DELETE FROM location_tags WHERE locationId IN (:locationIds)")
    suspend fun deleteTagsForLocationIds(locationIds: List<Int>)

    @Query(
        """
        SELECT tag
        FROM location_tags
        GROUP BY tag
        ORDER BY tag COLLATE NOCASE ASC
        """
    )
    fun getAllDistinctTags(): Flow<List<String>>

    @Query(
        """
        SELECT l.*
        FROM locations l
        INNER JOIN location_tags t
            ON l.id = t.locationId
        WHERE t.tag = :tag
        ORDER BY l.name COLLATE NOCASE ASC
        """
    )
    fun getLocationsForTag(tag: String): Flow<List<LocationEntity>>

    @Transaction
    suspend fun replaceLocationsAndTags(
        locations: List<LocationEntity>,
        tags: List<LocationTagEntity>,
    ) {
        if (locations.isEmpty()) return
        upsertLocations(locations)
        deleteTagsForLocationIds(locations.map { it.id })
        if (tags.isNotEmpty()) {
            upsertLocationTags(tags)
        }
    }
}