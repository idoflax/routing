package com.aikit.routing.graph.impl

import com.aikit.routing.graph.RouteFinder
import org.springframework.stereotype.Component

@Component
class CountryRouteFinder(countryScorer: CountryScorer) : RouteFinder<Country>(countryScorer)
