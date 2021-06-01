package com.aikit.routing.controllers

import com.aikit.routing.IntegrationTest
import com.aikit.routing.exceptions.ErrorMessage
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.OK

internal class RoutingControllerIT : IntegrationTest() {

    @Test
    fun `should return correct route for CZE ITA`() {
        given(spec)
            .`when`()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .get("/routing/CZE/ITA")
            .then()
            .statusCode(OK.value())
            .extract().`as`(Array<String>::class.java).also { response ->
                assertThat(response).isNotNull
                assertThat(response).containsExactly("CZE", "AUT", "ITA")
            }
    }

    @Test
    fun `should return bad request for no route found`() {
        given(spec)
            .`when`()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .get("/routing/ABW/ITA")
            .then()
            .statusCode(BAD_REQUEST.value())
            .extract().`as`(ErrorMessage::class.java).also { response ->
                assertThat(response).isNotNull
                assertThat(response.message).isEqualTo("Route not possible")
            }
    }

    @Test
    fun `should return bad request for invalid country codes`() {
        given(spec)
            .`when`()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .get("/routing/HAHA/ITA")
            .then()
            .statusCode(BAD_REQUEST.value())
            .extract().`as`(ErrorMessage::class.java).also { response ->
                assertThat(response).isNotNull
                assertThat(response.message).isEqualTo("Invalid Country code: HAHA")
            }
    }
}
