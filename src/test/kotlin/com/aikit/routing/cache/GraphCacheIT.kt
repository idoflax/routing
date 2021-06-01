package com.aikit.routing.cache

import com.aikit.routing.IntegrationTest
import com.aikit.routing.client.CountryInfoClient
import com.aikit.routing.dtos.CountryDTO
import com.aikit.routing.graph.Graph
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate

internal class GraphCacheTest : IntegrationTest() {

    @MockkBean(relaxed = true)
    private lateinit var countryInfoClient: CountryInfoClient

    @Autowired
    private lateinit var graphCache: GraphCache

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    @Test
    fun `should cache graph`() {
        val countryDTO = CountryDTO("AAA", listOf(0.0, 0.0), listOf("BBB"))
        every { countryInfoClient.getCountryInfo() } returns arrayOf(countryDTO)

        graphCache.getCountryGraph()

        assertThat(redisTemplate.keys("*")).containsExactly("countries::graph")
        redisTemplate.opsForValue().get("countries::graph").let {
            assertThat(it).isInstanceOf(Graph::class.java)
        }
    }
}
