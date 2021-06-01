package com.aikit.routing.graph

import java.util.*

open class RouteFinder<NODE : Node>(
    private val scorer: Scorer<NODE>
) {
    fun findRoute(from: NODE, to: NODE, graph: Graph<NODE>): List<NODE>? {
        val openSet = PriorityQueue<Route<NODE>>()
        val allNodes = mutableMapOf<NODE, Route<NODE>>()

        val start = Route(from, null, 0.0, scorer.score(from, to))
        openSet.add(start)
        allNodes[from] = start
        while (!openSet.isEmpty()) {
            val next = openSet.poll()
            if (next.current == to) {
                val route = ArrayList<NODE>()
                var current = next
                do {
                    route.add(0, current.current)
                    current = allNodes[current.previous]
                } while (current != null)
                return route
            }
            addAnotherRoute(graph, next, allNodes, to, openSet)
        }
        return null
    }

    private fun addAnotherRoute(
        graph: Graph<NODE>,
        next: Route<NODE>,
        allNodes: MutableMap<NODE, Route<NODE>>,
        to: NODE,
        openSet: PriorityQueue<Route<NODE>>
    ) {
        graph.getConnections(next.current)?.forEach { connection ->
            val nextRoute = allNodes[connection] ?: Route(connection)
            allNodes[connection] = nextRoute
            val newScore =
                next.score + scorer.score(next.current, connection)
            if (newScore < nextRoute.score) {
                nextRoute.previous = next.current
                nextRoute.score = newScore
                nextRoute.estimatedScore = newScore + scorer.score(connection, to)
                openSet.add(nextRoute)
            }
        }
    }
}
