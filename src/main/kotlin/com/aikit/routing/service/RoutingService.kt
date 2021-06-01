package com.aikit.routing.service

import com.aikit.routing.cache.GraphCache
import com.aikit.routing.exceptions.RouteNotPossibleException
import com.aikit.routing.graph.impl.CountryRouteFinder
import org.springframework.stereotype.Service

@Service
class RoutingService(
    private val graphCache: GraphCache,
    private val routeFinder: CountryRouteFinder
) {

    fun getRoute(origin: String, destination: String) =
        graphCache.getCountryGraph().let { graph ->
            routeFinder.findRoute(graph.getNode(origin), graph.getNode(destination), graph)?.map { it.id }
                ?: throw RouteNotPossibleException()
        }
}
