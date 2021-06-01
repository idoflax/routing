package com.aikit.routing.cache

import com.aikit.routing.client.CountryInfoClient
import com.aikit.routing.graph.Graph
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository

@Repository
class GraphCache(
    private val countryInfoClient: CountryInfoClient
) {

    @Cacheable("countries", key = "'graph'")
    fun getCountryGraph() =
        countryInfoClient.getCountryInfo().let { countries ->
            Graph(
                nodes = countries.map { it.toCountry() }.toSet(),
                connections = countries.associate { it.cca3 to it.borders.toSet() }
            )
        }
}
