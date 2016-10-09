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

typealias Screen = Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>

enum class Direction {
    BACK, FORWARD
}

private val SCREEN_FACTORY: Union6.Factory<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>
        = GenericUnions.sextetFactory()

sealed class Screens(open val id: String = "")

data class Home(override val id: String = "") : Screens(id)

data class RotationExample(override val id: String = "") : Screens(id)

data class ListExample(override val id: String = "") : Screens(id)

data class DragAndDropExample(override val id: String = "") : Screens(id)

data class CacheExample(override val id: String = "") : Screens(id)

data class PaginationExample(override val id: String = "") : Screens(id)

fun createHome(): Screen = SCREEN_FACTORY.first(Home())

fun createRotation(): Screen = SCREEN_FACTORY.second(RotationExample())

fun createList(): Screen = SCREEN_FACTORY.third(ListExample())

fun createCache(): Screen = SCREEN_FACTORY.fourth(CacheExample())

fun createDragAndDrop(): Screen = SCREEN_FACTORY.fifth(DragAndDropExample())

fun createPagination(): Screen = SCREEN_FACTORY.sixth(PaginationExample())