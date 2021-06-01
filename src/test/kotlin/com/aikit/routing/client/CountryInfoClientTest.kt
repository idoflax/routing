package com.aikit.routing.client

import com.aikit.routing.IntegrationTest
import com.aikit.routing.cache.GraphCacheInitializer
import com.aikit.routing.dtos.CountryDTO
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.common.Slf4jNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.ninjasquad.springmockk.MockkBean
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration

@TestInstance(PER_CLASS)
@ContextConfiguration(initializers = [TestProperties::class])
internal class CountryInfoClientTest : IntegrationTest() {

    @Autowired
    private lateinit var countryInfoClient: CountryInfoClient

    @MockkBean
    private lateinit var graphCacheInitializer: GraphCacheInitializer

    protected val wireMockServer = WireMockServer(
        WireMockConfiguration.wireMockConfig()
            .notifier(Slf4jNotifier(false))
            .disableRequestJournal()
            .port(8000)
    )

    @BeforeAll
    fun startWireMock() {
        wireMockServer.start()
        WireMock.configureFor(8000)
    }

    @AfterAll
    fun stopWireMock() = wireMockServer.stop()

    @Test
    fun `should process countries input`() {
        assertThat(countryInfoClient.getCountryInfo()).containsExactly(
            CountryDTO(
                cca3 = "ABW", latlng = listOf(
                    12.5,
                    -69.96666666
                ), borders = listOf()
            ), CountryDTO(
                cca3 = "AFG", latlng = listOf(
                    33.00,
                    65.00
                ), borders = listOf("IRN", "PAK", "TKM", "UZB", "TJK", "CHN")
            )

        )
    }
}

internal class TestProperties : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        val values = TestPropertyValues.of(
            "country-info-url=http://localhost:8000/countries.json"
        )
        values.applyTo(applicationContext)
    }
}
