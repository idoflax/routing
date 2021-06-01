package com.aikit.routing.graph.impl

import com.aikit.routing.graph.Node
import java.io.Serializable

data class Country(
    override val id: String,
    val latitude: Double,
    val longitude: Double
) : Node, Serializable
