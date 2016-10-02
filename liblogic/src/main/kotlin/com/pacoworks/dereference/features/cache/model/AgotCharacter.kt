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

typealias AgotCharacter = Union2<UnknownAgotCharacter, KnownAgotCharacter>

data class UnknownAgotCharacter(val id: String, val reason: UnknownReason)

typealias UnknownReason = Union3<Unavailable, NetworkError, IncorrectInfo>

object Unavailable

object NetworkError

object IncorrectInfo

data class KnownAgotCharacter(val id: String, val name: String, val titles: List<String>)

private val CHAR_GENERATOR: Union2.Factory<UnknownAgotCharacter, KnownAgotCharacter> = GenericUnions.doubletFactory()

private val REASON_FACTORY: Union3.Factory<Unavailable, NetworkError, IncorrectInfo> = GenericUnions.tripletFactory()

fun createUnknownUnavailableCharacter(id: String): AgotCharacter =
        CHAR_GENERATOR.first(UnknownAgotCharacter(id, REASON_FACTORY.first(Unavailable)))

fun createUnknownNetworkErrorCharacter(id: String): AgotCharacter =
        CHAR_GENERATOR.first(UnknownAgotCharacter(id, REASON_FACTORY.second(NetworkError)))

fun createUnknownIncorrectCharacter(id: String): AgotCharacter =
        CHAR_GENERATOR.first(UnknownAgotCharacter(id, REASON_FACTORY.third(IncorrectInfo)))


fun createKnownCharacter(id: String, name: String, titles: List<String>): AgotCharacter =
        CHAR_GENERATOR.second(KnownAgotCharacter(id, name, titles))
