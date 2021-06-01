package com.aikit.routing

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import mu.KotlinLogging.logger
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.FixedHostPortGenericContainer

internal class KGenericContainer(imageName: String) : FixedHostPortGenericContainer<KGenericContainer>(imageName)

@ActiveProfiles("integration-test")
@ContextConfiguration(initializers = [RedisInitializer::class])
@ExtendWith(RestDocumentationExtension::class)
@AutoConfigureRestDocs
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [RoutingApplication::class]
)
abstract class IntegrationTest {

    @LocalServerPort
    private val port: Int = 0

    lateinit var spec: RequestSpecification

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        spec = restAssuredSpecification(restDocumentation, port)
    }

    fun restAssuredSpecification(restDocumentation: RestDocumentationContextProvider, port: Int): RequestSpecification {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        return RequestSpecBuilder()
            .addFilter(
                RestAssuredRestDocumentation.documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(Preprocessors.removeHeaders("Host", "Content-Length"))
                    .withResponseDefaults(
                        Preprocessors.removeHeaders(
                            "X-Content-Type-Options",
                            "X-Content-Type",
                            "X-XSS-Protection",
                            "Cache-Control",
                            "Pragma",
                            "Expires",
                            "X-Frame-Options"
                        )
                    )
            )
            .setBaseUri("http://localhost")
            .setPort(port)
            .build()
    }
}

open class RedisInitializer(private val extraPropertyValues: Map<String, String> = emptyMap()) :
    ApplicationContextInitializer<ConfigurableApplicationContext> {

    companion object {
        private val redis = KGenericContainer("redis:3-alpine")
        private val logger = logger { }

        init {
            with(redis) {
                withLabel("name", "redis")
                withExposedPorts(6379)
            }
            redis.start()
            logger.info("${redis.labels["name"]} is starting at: ${redis.containerIpAddress}:${redis.firstMappedPort}")
        }
    }

    override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
        var values = TestPropertyValues.of(
            "spring.redis.host=${redis.containerIpAddress}",
            "spring.redis.port=${redis.getMappedPort(6379)}"
        )
        extraPropertyValues.forEach { values = values.and("${it.key}=${it.value}") }
        values.applyTo(configurableApplicationContext)
    }
}
