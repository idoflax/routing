package com.aikit.routing.client

import com.aikit.routing.dtos.CountryDTO
import com.aikit.routing.exceptions.ResponseException
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging.logger
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class CountryInfoClient(
    private val okHttpClient: OkHttpClient,
    private val objectMapper: ObjectMapper,
    @Value("\${country-info-url}")
    private val countryInfoUrl: String
) {

    fun getCountryInfo() =
        Request.Builder().url(countryInfoUrl).get().build().let {
            okHttpClient.newCall(it)
        }.execute().let { processResponse(it) }

    private fun processResponse(response: Response) =
        if (response.isSuccessful) objectMapper.okResponseAs(
            response,
            Array<CountryDTO>::class.java
        ) else response.throwResponseException()


    private fun <T> ObjectMapper.okResponseAs(response: Response, c: Class<T>): T =
        readValue(response.body?.string(), c).also { response.closeSafely() }

    private fun Response.closeSafely() = try {
        close()
    } catch (e: Exception) {
        logger.warn("Failed attempt to close request: ${e.message}, $request")
    }

    private fun Response.throwResponseException(messagePrefix: String? = null): Nothing = throw ResponseException(
        "${messagePrefix?.let { "$it: " } ?: ""} error response from request: ${request}, response: ${this}\n$message",
        response = this
    ).also { closeSafely() }


    companion object {
        private val logger = logger {}
    }
}
