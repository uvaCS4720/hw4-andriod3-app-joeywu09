package edu.nd.pmcburne.hello.data

import com.google.gson.annotations.SerializedName

data class PlacemarkDto(
    val id: Int,
    val name: String,
    val description: String,
    @SerializedName("tag_list")
    val tagList: List<String>,
    @SerializedName("visual_center")
    val visualCenter: VisualCenterDto,
)

data class VisualCenterDto(
    val latitude: Double,
    val longitude: Double,
)

fun PlacemarkDto.toEntity(): LocationEntity = LocationEntity(
    id = id,
    name = name,
    description = description,
    latitude = visualCenter.latitude,
    longitude = visualCenter.longitude,
)

fun PlacemarkDto.toTagEntities(): List<LocationTagEntity> =
    tagList.map { tag ->
        LocationTagEntity(
            locationId = id,
            tag = tag,
        )
    }
