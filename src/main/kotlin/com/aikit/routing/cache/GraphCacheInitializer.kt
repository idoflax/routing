package com.aikit.routing.cache

import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class GraphCacheInitializer(
    private val graphCache: GraphCache
) : InitializingBean {

    override fun afterPropertiesSet() {
        graphCache.getCountryGraph()
    }
}
