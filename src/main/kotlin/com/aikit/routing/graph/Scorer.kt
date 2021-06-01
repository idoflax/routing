package com.aikit.routing.graph

interface Scorer<T : Node> {
    fun score(from: T, to: T): Double
}
