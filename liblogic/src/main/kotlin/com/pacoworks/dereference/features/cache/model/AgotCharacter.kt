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

package com.pacoworks.dereference.features.cache.model

import com.pacoworks.rxsealedunions.Union2
import com.pacoworks.rxsealedunions.Union3
import com.pacoworks.rxsealedunions.generic.GenericUnions

/**
 * Algebra representing a Game of Thrones character
 */
typealias AgotCharacter = Union2<UnknownAgotCharacter, KnownAgotCharacter>

/**
 * Data class representing an unknown Game of Thrones character that's not available yet
 */
data class UnknownAgotCharacter(val id: String, val reason: UnknownReason)

/**
 * Algebra representing the reason why a Game of Thrones character isn't available yet
 */
typealias UnknownReason = Union3<Unavailable, NetworkError, IncorrectInfo>

/**
 * The reason the character isn't available is that it's being requested
 */
object Unavailable

/**
 * The reason the character isn't available is a network error
 */
object NetworkError

/**
 * The reason the character isn't available is that the network returned an invalid character
 */
object IncorrectInfo

data class KnownAgotCharacter(val id: String, val name: String, val titles: List<String>)

private val CHAR_GENERATOR: Union2.Factory<UnknownAgotCharacter, KnownAgotCharacter> = GenericUnions.doubletFactory()

private val REASON_FACTORY: Union3.Factory<Unavailable, NetworkError, IncorrectInfo> = GenericUnions.tripletFactory()

/**
 * Constructor for unknown characters that are not available yet
 *
 * @param id the id
 * @return an unavailable character
 */
fun createUnknownUnavailableCharacter(id: String): AgotCharacter =
        CHAR_GENERATOR.first(UnknownAgotCharacter(id, REASON_FACTORY.first(Unavailable)))

/**
 * Constructor for unknown characters that came after a network error
 *
 * @param id the id
 * @return an unknown character
 */
fun createUnknownNetworkErrorCharacter(id: String): AgotCharacter =
        CHAR_GENERATOR.first(UnknownAgotCharacter(id, REASON_FACTORY.second(NetworkError)))

/**
 * Constructor for unknown characters that came incorrectly from the server
 *
 * @param id the id
 * @return an unknown character
 */
fun createUnknownIncorrectCharacter(id: String): AgotCharacter =
        CHAR_GENERATOR.first(UnknownAgotCharacter(id, REASON_FACTORY.third(IncorrectInfo)))

/**
 * Constructor for characters that have been identified by the server
 *
 * @param id the id
 * @param name the name
 * @param titles the titles
 * @return a character
 */
fun createKnownCharacter(id: String, name: String, titles: List<String>): AgotCharacter =
        CHAR_GENERATOR.second(KnownAgotCharacter(id, name, titles))
