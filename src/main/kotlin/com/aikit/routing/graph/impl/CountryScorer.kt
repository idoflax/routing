package com.aikit.routing.graph.impl

import com.aikit.routing.graph.Scorer
import org.springframework.stereotype.Component
import java.lang.Math.toRadians
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@Component
class CountryScorer : Scorer<Country> {

    override fun score(from: Country, to: Country): Double {
        val dLat = toRadians(to.latitude - from.latitude)
        val dLon = toRadians(to.longitude - from.longitude)
        val lat1 = toRadians(from.latitude)
        val lat2 = toRadians(to.latitude)

        val a = (sin(dLat / 2).pow(2.0)
                + sin(dLon / 2).pow(2.0) * cos(lat1) * cos(lat2))
        val c = 2 * asin(sqrt(a))
        return EARTH_RADIUS * c
    }

    companion object {
        const val EARTH_RADIUS = 6372.8
    }
}
