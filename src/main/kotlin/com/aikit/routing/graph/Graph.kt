package com.aikit.routing.graph

import com.aikit.routing.exceptions.InvalidCountryCodeException
import java.io.Serializable


class Graph<T : Node>(
    private val nodes: Set<T> = mutableSetOf(),
    private val connections: Map<String, Set<String>> = mutableMapOf()
) : Serializable {

    fun getNode(id: String): T {
        return nodes
            .firstOrNull { node -> node.id == id } ?: kotlin.run {
            throw InvalidCountryCodeException(id)
        }
    }

    fun getConnections(node: T): Set<T>? {
        return connections[node.id]
            ?.map { id -> getNode(id) }?.toSet()
    }

}
