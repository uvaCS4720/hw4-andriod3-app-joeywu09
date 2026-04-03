package edu.nd.pmcburne.hello.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
)

@Entity(
    tableName = "location_tags",
    primaryKeys = ["locationId", "tag"],
)
data class LocationTagEntity(
    val locationId: Int,
    val tag: String,
)

data class CampusLocation(
    val id: Int,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
)

fun LocationEntity.toCampusLocation(): CampusLocation = CampusLocation(
    id = id,
    name = name,
    description = description,
    latitude = latitude,
    longitude = longitude,
)