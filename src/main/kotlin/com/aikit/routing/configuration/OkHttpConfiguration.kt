package com.aikit.routing.configuration

import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit.SECONDS

@Configuration
class OkHttpConfiguration {
    companion object {
        const val DEFAULT_TIMEOUT = 60L
    }

    @Bean
    fun okHttpClient() = OkHttpClient.Builder()
        .connectTimeout(DEFAULT_TIMEOUT, SECONDS)
        .build()
}
