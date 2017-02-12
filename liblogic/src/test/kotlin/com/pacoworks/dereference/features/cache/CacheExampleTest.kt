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

import com.jakewharton.rxrelay.PublishRelay
import com.jakewharton.rxrelay.SerializedRelay
import com.pacoworks.dereference.bindAsTest
import com.pacoworks.dereference.features.cache.model.KnownAgotCharacter
import com.pacoworks.dereference.features.cache.model.UnknownAgotCharacter
import com.pacoworks.dereference.mockView
import com.pacoworks.rxsealedunions.Union2
import rx.Observable
import rx.functions.Action2
import java.util.concurrent.atomic.AtomicLong


class MockCacheExampleOutputView : CacheExampleOutputView {
    val filter: PublishRelay<String> = PublishRelay.create<String>()

    override fun filterSelected(): Observable<String> =
            filter
}

class MockCacheExampleInputView : CacheExampleInputView {

    val characterInfoCount = AtomicLong()

    val filterListCount = AtomicLong()

    val currentFilterCount = AtomicLong()

    override fun showCharacterInfo(info: Union2<UnknownAgotCharacter, KnownAgotCharacter>) =
            mockView<Union2<UnknownAgotCharacter, KnownAgotCharacter>>(characterInfoCount).invoke(info)

    override fun filterList(ids: List<String>) =
            mockView<List<String>>(filterListCount).invoke(ids)

    override fun currentFilter(id: String) =
            mockView<String>(currentFilterCount).invoke(id)

    override fun <T: Any> createBinder(): Action2<SerializedRelay<T, T>, (T) -> Unit> =
            bindAsTest()
}