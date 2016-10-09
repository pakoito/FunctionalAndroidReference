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

import com.pacoworks.dereference.architecture.ui.createStateHolder
import org.junit.Test
import rx.observers.TestSubscriber

class CacheExampleSubscribeFilterTest {

    @Test
    fun SelectedFilter_NewFilterSelected_StateHasFilter() {
        val mockCacheExampleOutputView = MockCacheExampleOutputView()
        val originalValue = "Original"
        val newValue = "Paco"
        val currentIdRelay = createStateHolder(originalValue)
        val testSubscriber = TestSubscriber<String>()
        /* Assert view not called */
        testSubscriber.assertValueCount(0)
        /* Bind view to state */
        currentIdRelay.subscribe(testSubscriber)
        handleSelectedState(mockCacheExampleOutputView, currentIdRelay)
        /* Assert view has taken originalValue */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValues(originalValue)
        /* Call with newValue */
        mockCacheExampleOutputView.filter.call(newValue)
        /* Assert view has received newValue */
        testSubscriber.assertValueCount(2)
        testSubscriber.assertValues(originalValue, newValue)
        testSubscriber.assertNoTerminalEvent()
    }

    @Test
    fun SelectedFilter_SelectSame_StateDoesNotChange() {
        val mockCacheExampleOutputView = MockCacheExampleOutputView()
        val originalValue = "Original"
        val currentIdRelay = createStateHolder(originalValue)
        val testSubscriber = TestSubscriber<String>()
        /* Assert view not called */
        testSubscriber.assertValueCount(0)
        /* Bind view to state */
        currentIdRelay.subscribe(testSubscriber)
        handleSelectedState(mockCacheExampleOutputView, currentIdRelay)
        val newValue = "New"
        /* Call with originalValue */
        mockCacheExampleOutputView.filter.call(newValue)
        /* Assert view has taken originalValue */
        testSubscriber.assertValueCount(2)
        testSubscriber.assertValues(originalValue, newValue)
        /* Call with newValue */
        mockCacheExampleOutputView.filter.call(newValue)
        /* Assert view has received newValue */
        testSubscriber.assertValueCount(2)
        testSubscriber.assertValues(originalValue, newValue)
        testSubscriber.assertNoTerminalEvent()
    }

}