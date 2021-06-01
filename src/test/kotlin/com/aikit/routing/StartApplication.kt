package com.aikit.routing

import org.springframework.boot.builder.SpringApplicationBuilder

/**
 * Entry point for starting application locally.
 * Before application starts it starts also all dependencies like database, cache etc
 * as Docker containers using Test Containers library.
 */
class StartApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplicationBuilder(RoutingApplication::class.java)
                .profiles("local")
                .initializers(RedisInitializer())
                .run(*args)
        }
    }
}
