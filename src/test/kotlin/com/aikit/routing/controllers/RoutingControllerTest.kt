package com.aikit.routing.controllers

import com.aikit.routing.exceptions.InvalidCountryCodeException
import com.aikit.routing.exceptions.RouteNotPossibleException
import com.aikit.routing.service.RoutingService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(RoutingController::class)
internal class RoutingControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean(relaxed = true)
    private lateinit var routingService: RoutingService

    @Test
    fun `should return ok for valid route`() {
        every { routingService.getRoute("AAA", "BBB") } returns listOf("AAA", "BBB")

        mockMvc.perform(
            MockMvcRequestBuilders.get("/routing/AAA/BBB")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)

        verify(exactly = 1) { routingService.getRoute("AAA", "BBB") }
    }

    @Test
    fun `should return bad request for no route found`() {
        every { routingService.getRoute("AAA", "BBB") } throws RouteNotPossibleException()

        mockMvc.perform(
            MockMvcRequestBuilders.get("/routing/AAA/BBB")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest)

        verify(exactly = 1) { routingService.getRoute("AAA", "BBB") }
    }

    @Test
    fun `should return bad request for invalid country codes`() {
        every { routingService.getRoute("AAA", "BBB") } throws InvalidCountryCodeException("AAA")

        mockMvc.perform(
            MockMvcRequestBuilders.get("/routing/AAA/BBB")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest)

        verify(exactly = 1) { routingService.getRoute("AAA", "BBB") }
    }
}
