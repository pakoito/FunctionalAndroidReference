/*
 * Copyright (c) pakoito 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pacoworks.dereference.features.global

import com.pacoworks.rxsealedunions.Union6
import com.pacoworks.rxsealedunions.generic.GenericUnions

/**
 * Algebra representing all possible screens the app can be at
 */
typealias Screen = Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>

/**
 * Enum class defining the direction a transition goes
 */
enum class Direction {
    BACK, FORWARD
}

private val SCREEN_FACTORY: Union6.Factory<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>
        = GenericUnions.sextetFactory()

sealed class Screens(open val id: String = "")

/**
 * Data class representing the Home [Screen]
 */
data class Home(override val id: String = "") : Screens(id)

/**
 * Data class representing the RotationExample [Screen]
 */
data class RotationExample(override val id: String = "") : Screens(id)

/**
 * Data class representing the ListExample [Screen]
 */
data class ListExample(override val id: String = "") : Screens(id)

/**
 * Data class representing the DragAndDropExample [Screen]
 */
data class DragAndDropExample(override val id: String = "") : Screens(id)

/**
 * Data class representing the CacheExample [Screen]
 */
data class CacheExample(override val id: String = "") : Screens(id)

/**
 * Data class representing the PaginationExample [Screen]
 */
data class PaginationExample(override val id: String = "") : Screens(id)

/**
 * Constructor for a Home [Screen]
 */
fun createHome(): Screen = SCREEN_FACTORY.first(Home())

/**
 * Constructor for a Rotation [Screen]
 */
fun createRotation(): Screen = SCREEN_FACTORY.second(RotationExample())

/**
 * Constructor for a List [Screen]
 */
fun createList(): Screen = SCREEN_FACTORY.third(ListExample())

/**
 * Constructor for a Cache [Screen]
 */
fun createCache(): Screen = SCREEN_FACTORY.fourth(CacheExample())

/**
 * Constructor for a DragAndDrop [Screen]
 */
fun createDragAndDrop(): Screen = SCREEN_FACTORY.fifth(DragAndDropExample())

/**
 * Constructor for a Pagination [Screen]
 */
fun createPagination(): Screen = SCREEN_FACTORY.sixth(PaginationExample())