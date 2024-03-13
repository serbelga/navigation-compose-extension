package dev.sergiobelda.navigation.compose.extension.sample.ui.search

import dev.sergiobelda.navigation.compose.extension.NavArgumentKey
import dev.sergiobelda.navigation.compose.extension.NavRoute
import dev.sergiobelda.navigation.compose.extension.TopLevelNavDestination

object SearchNavDestination : TopLevelNavDestination<NavArgumentKey>() {
    override val destinationId: String = "search"
}

object SearchNavRoute : NavRoute<NavArgumentKey>(destination = SearchNavDestination)
