package com.aikit.routing.dtos

import com.aikit.routing.graph.impl.Country
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class CountryDTO(
    val cca3: String,
    val latlng: List<Double>,
    val borders: List<String>
) {
    fun toCountry() = Country(cca3, latlng[0], latlng[1])
}

