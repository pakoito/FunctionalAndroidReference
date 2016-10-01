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

package com.pacoworks.dereference.architecture.ui

import com.pacoworks.rxsealedunions.Union2
import com.pacoworks.rxsealedunions.generic.GenericUnions

typealias Screen = Union2<Home, Rotation>

enum class Direction {
    BACK, FORWARD
}

private val SCREEN_FACTORY: Union2.Factory<Home, Rotation> = GenericUnions.doubletFactory()

sealed class Screens(open val id: String = "")

data class Home(override val id: String = ""): Screens(id)

data class Rotation(override val id: String = ""): Screens(id)

fun createHome(): Screen = SCREEN_FACTORY.first(Home())

fun createRotation(): Screen = SCREEN_FACTORY.second(Rotation())