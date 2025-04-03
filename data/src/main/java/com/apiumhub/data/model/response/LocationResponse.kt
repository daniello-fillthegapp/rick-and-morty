package com.apiumhub.data.model.response

import com.apiumhub.domain.model.LocationModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationResponse(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String,
) {
    fun toDomain(): LocationModel {
        return LocationModel(
            name = this.name,
            url = this.url
        )
    }
}