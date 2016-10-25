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
import com.pacoworks.dereference.features.cache.model.AgotCharacter
import com.pacoworks.dereference.features.cache.services.CacheRequest
import com.pacoworks.rxcomprehensions.RxComprehensions.doSM
import rx.Observable
import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * Binds the state of this use case to a [com.pacoworks.dereference.architecture.ui.BoundView]
 *
 * @see [com.pacoworks.dereference.architecture.ui.bind]
 */
fun bindCacheExample(viewInput: CacheExampleInputView, state: CacheExampleState) {
    viewInput.createBinder<AgotCharacter>().call(state.currentCharacter, viewInput::showCharacterInfo)
    viewInput.createBinder<List<String>>().call(state.ids, viewInput::filterList)
    viewInput.createBinder<String>().call(state.currentId, viewInput::currentFilter)
}

/**
 * Subscribes all use cases in the file
 */
fun subscribeCacheExampleInteractor(viewOutputView: CacheExampleOutputView, state: CacheExampleState, server: CacheRequest): Subscription =
        CompositeSubscription(
                handleFilterChange(server, state, state.currentId),
                handleSelectedState(viewOutputView, state.currentId))

fun handleFilterChange(server: CacheRequest, state: CacheExampleState, currentId: StateHolder<String>): Subscription =
        doSM(
                /* For every id selected */
                { currentId.distinctUntilChanged() },
                /* Get the current cache of characters */
                { state.characterCache.first() },
                { id, cache ->
                    cache[id]!!.let {
                        character ->
                        character.join(
                                /* If the character is unavailable, request it to network */
                                { unknown -> updateFromNetwork(unknown.id, cache, server, state.characterCache).startWith(character) },
                                /* If the character is in the cache, immediately forward it */
                                { present -> Observable.just(character) })
                    }
                }
        ).subscribe(state.currentCharacter)

private fun updateFromNetwork(id: String, cache: AgotCharacterCache, server: CacheRequest, characterCache: StateHolder<AgotCharacterCache>) =
        server.invoke(id)
                /* Update cache with new value */
                .map { result -> cache.plus(id to result) }
                .doOnNext(characterCache)
                /* Return new result from cache */
                .map { it[id] }

fun handleSelectedState(viewOutputView: CacheExampleOutputView, currentId: StateHolder<String>): Subscription =
        /* When a new id is selected */
        viewOutputView.filterSelected().distinctUntilChanged().subscribe(currentId)