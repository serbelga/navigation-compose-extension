package dev.sergiobelda.navigation.compose.extension

import androidx.navigation.NamedNavArgument

/**
 * Represents the navigation route to reach some destination. [NavAction.navigate] receives a
 * [NavRoute] object.
 *
 * @param destination Navigation destination.
 * @param arguments List of arguments passed in this route.
 */
abstract class NavRoute<K>(
    val destination: NavDestination<K>,
    private val arguments: Map<K, Any?> = emptyMap()
) where K : NavArgumentKey {

    /**
     * Navigation route. It consists of [destination] id and the [arguments] values.
     */
    internal val route: String =
        destination.destinationId.addArgumentsValues()

    /**
     * The [arguments] transformed into a Map<String, Any?> where the key is the argument string key.
     */
    private val argumentsKeyStringMap: Map<String, Any?>
        get() = arguments.mapKeys { it.key.argumentKey }

    /**
     * Returns the part of the route that contains the arguments values.
     */
    private fun String.addArgumentsValues(): String {
        val optionalParameters = destination.arguments.filter {
            it.argument.isDefaultValuePresent || it.argument.isNullable
        }
        val parameters = destination.arguments.filter {
            !it.argument.isDefaultValuePresent && !it.argument.isNullable
        }

        return this + buildString {
            // Add required parameters separated by "/".
            parameters.takeIf { it.isNotEmpty() }?.forEach { namedNavArgument ->
                if (argumentsKeyStringMap.containsKey(namedNavArgument.name)) {
                    append(PARAM_SEPARATOR)
                    append(argumentsKeyStringMap[namedNavArgument.name].toString())
                } else throw Exception("Not present in arguments")
            } ?: append(PARAM_SEPARATOR)
            optionalParameters.takeIf { it.isNotEmpty() }?.let { list ->
                appendOptionalParameters(list)
            }
        }
    }

    /**
     * Generate the string from the list of optional parameters and append if optional parameters
     * have been added. For example:
     * - Given no optional parameters, the result will be an empty string.
     * - Given one optional parameter, `param1`, the result will be: ?param1=value1.
     * - Given multiple optional parameters, `param1` and `param2`, the result will be: ?param1=value1&param2=value2.
     *
     * @throws IllegalArgumentException if an argument with a key is not nullable and its value is null.
     */
    private fun StringBuilder.appendOptionalParameters(list: List<NamedNavArgument>) {
        append(
            list.joinToString(
                prefix = QUERY_PARAM_PREFIX,
                separator = QUERY_PARAM_SEPARATOR
            ) { namedNavArgument ->
                // Check if the argument is present in the arguments map, if not, check if it has a default value.
                if (argumentsKeyStringMap.containsKey(namedNavArgument.name)) {
                    val value = argumentsKeyStringMap[namedNavArgument.name]
                    when {
                        value != null -> {
                            "${namedNavArgument.name}=${argumentsKeyStringMap[namedNavArgument.name]}"
                        }

                        !namedNavArgument.argument.isNullable -> {
                            throw IllegalArgumentException("Argument with key ${namedNavArgument.name} is not nullable.")
                        }

                        else -> ""
                    }
                } else {
                    val defaultValue = namedNavArgument.argument.defaultValue
                    when {
                        defaultValue != null -> {
                            "${namedNavArgument.name}=${namedNavArgument.argument.defaultValue}"
                        }

                        !namedNavArgument.argument.isNullable -> {
                            throw IllegalArgumentException("Argument with key ${namedNavArgument.name} is not nullable.")
                        }

                        else -> ""
                    }
                }
            }.takeIf { it != QUERY_PARAM_PREFIX }.orEmpty()
        )
    }
}
