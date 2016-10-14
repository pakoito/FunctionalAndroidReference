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

package com.pacoworks.dereference.features.cache

import com.pacoworks.dereference.architecture.ui.StateHolder
import com.pacoworks.dereference.architecture.ui.createStateHolder
import com.pacoworks.dereference.features.cache.model.AgotCharacter
import com.pacoworks.dereference.features.cache.model.createUnknownUnavailableCharacter
import rx.Observable

/**
 * Map class containing [AgotCharacter] indexed by their id
 */
typealias AgotCharacterCache = Map<String, AgotCharacter>

/**
 * Contains all [StateHolder] objects used to represent state in this screen
 */
data class CacheExampleState(
        val ids: StateHolder<List<String>> = createStateHolder(CHARACTER_IDS),
        val currentId: StateHolder<String> = createStateHolder(initialId),
        val characterCache: StateHolder<AgotCharacterCache> = createStateHolder(CHARACTERS),
        val currentCharacter: StateHolder<AgotCharacter> = createStateHolder(initialCharacter)
)

private val CHARACTERS: AgotCharacterCache = Observable.range(30, 100).map { createUnknownUnavailableCharacter(it.toString()) }.toMap { it.join({ it.id }, { it.id }) }.toBlocking().first()

private val CHARACTER_IDS: List<String> = Observable.range(30, 100).map { it.toString() }.toList().toBlocking().first().toList()

private val initialId: String = CHARACTER_IDS[0]

private val initialCharacter: AgotCharacter = CHARACTERS[initialId]!!
