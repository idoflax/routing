package com.aikit.routing.graph

class Route<T : Node>(
    var current: T,
    var previous: T?,
    var score: Double,
    var estimatedScore: Double
) : Comparable<Route<T>> {

    override fun compareTo(other: Route<T>): Int =
        estimatedScore.compareTo(other.estimatedScore)

    constructor(current: T) : this(current, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)

}
