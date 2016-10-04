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
import com.pacoworks.rxcomprehensions.RxComprehensions.doSM
import rx.Observable
import rx.Subscription
import rx.subscriptions.CompositeSubscription

fun bindCacheExample(viewInput: CacheExampleInputView, state: CacheExampleState) {
    viewInput.createBinder<AgotCharacter>().call(state.currentCharacter, viewInput::showCharacterInfo)
    viewInput.createBinder<List<String>>().call(state.ids, viewInput::filterList)
    viewInput.createBinder<String>().call(state.currentId, viewInput::currentFilter)
}

typealias CacheRequest = (String) -> Observable<AgotCharacter>

fun subscribeCacheExampleInteractor(viewOutputView: CacheExampleOutputView, state: CacheExampleState, server: CacheRequest): Subscription =
        CompositeSubscription(
                handleFilterChange(server, state, viewOutputView),
                handleSelectedState(viewOutputView, state.currentId))

private fun handleFilterChange(server: CacheRequest, state: CacheExampleState, viewOutputView: CacheExampleOutputView): Subscription =
        doSM(
                { viewOutputView.filterSelected().distinctUntilChanged() },
                { state.characterCache.first() },
                { id, cache ->
                    cache[id]!!.let {
                        character ->
                        character.join(
                                { unknown -> updateFromNetwork(unknown.id, cache, server, state.characterCache).startWith(character) },
                                { present -> Observable.just(character) })
                    }
                }
        ).subscribe(state.currentCharacter)

private fun updateFromNetwork(id: String, cache: AgotCharacterCache, server: CacheRequest, characterCache: StateHolder<AgotCharacterCache>) =
        server.invoke(id)
                /* Update cache */
                .map { result -> cache.plus(id to result) }
                .doOnNext(characterCache)
                /* Return new result from cache */
                .map { it[id] }

private fun handleSelectedState(viewOutputView: CacheExampleOutputView, currentId: StateHolder<String>): Subscription =
        viewOutputView.filterSelected().distinctUntilChanged().subscribe(currentId)