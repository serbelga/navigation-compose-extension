/*
 * Copyright 2024 Sergio Belda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.sergiobelda.navigation.compose.extended.compiler.processor.generator

import com.google.devtools.ksp.symbol.KSValueParameter

/**
 * Represents a navigation argument in a navigation destination. It is defined by the
 * navigation argument's [name], [defaultValue] and [parameter] data.
 */
internal data class NavArgumentParameter(
    val name: String,
    val defaultValue: String,
    val parameter: KSValueParameter,
) {

    /**
     * Returns true if the navigation argument has a default value.
     */
    val hasDefaultValue: Boolean
        get() = defaultValue.isNotBlank()
}
