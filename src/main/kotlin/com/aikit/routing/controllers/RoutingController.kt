package com.aikit.routing.controllers

import com.aikit.routing.service.RoutingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("routing")
class RoutingController(
    private val routingService: RoutingService
) {

    @Operation(description = "Best land routing")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "Success")])
    @GetMapping("/{origin}/{destination}")
    fun getRoute(@PathVariable origin: String, @PathVariable destination: String) =
        ResponseEntity.ok(routingService.getRoute(origin, destination))

}
