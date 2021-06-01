package com.aikit.routing.service

import com.aikit.routing.cache.GraphCache
import com.aikit.routing.exceptions.RouteNotPossibleException
import com.aikit.routing.graph.Graph
import com.aikit.routing.graph.impl.Country
import com.aikit.routing.graph.impl.CountryRouteFinder
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class RoutingServiceTest {

    private val graph = mockk<Graph<Country>>(relaxed = true)
    private val graphCache = mockk<GraphCache>(relaxed = true)
    private val routeFinder = mockk<CountryRouteFinder>(relaxed = true)

    private val routingService = RoutingService(graphCache, routeFinder)

    @Test
    fun `should return route when one is possible`() {
        every { graphCache.getCountryGraph() } returns graph
        every { graph.getNode(any()) } returns Country("", 0.0, 0.0)
        every { routeFinder.findRoute(any(), any(), graph) } returns listOf(Country("AAA", 0.0, 0.0))

        val route = routingService.getRoute("BBB", "CCC")

        assertThat(route).containsExactly("AAA")
    }

    @Test
    fun `should throw RouteNotPossibleException when no route is possible`() {
        every { graphCache.getCountryGraph() } returns graph
        every { graph.getNode(any()) } returns Country("", 0.0, 0.0)
        every { routeFinder.findRoute(any(), any(), graph) } returns null

        assertThrows<RouteNotPossibleException> { routingService.getRoute("BBB", "CCC") }
    }

}
