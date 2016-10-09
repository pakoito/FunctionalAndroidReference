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
import com.pacoworks.dereference.features.cache.model.createKnownCharacter
import org.junit.Assert.assertEquals
import org.junit.Test

class CacheExampleBindTest {

    @Test
    fun characterInfo_BindInput_InfoDisplayed() {
        val mockCacheExampleInputView: MockCacheExampleInputView = MockCacheExampleInputView()
        /* Pre-bind: assert view not called */
        assertEquals(0L, mockCacheExampleInputView.characterInfoCount.get())
        /* Bind */
        bindCacheExample(mockCacheExampleInputView, CacheExampleState(currentCharacter = createStateHolder(createKnownCharacter("", "", listOf()))))
        /* Assert view gets called with current state */
        assertEquals(1L, mockCacheExampleInputView.characterInfoCount.get())
    }

    @Test
    fun filterList_BindInput_InfoDisplayed() {
        val mockCacheExampleInputView: MockCacheExampleInputView = MockCacheExampleInputView()
        /* Pre-bind: assert view not called */
        assertEquals(0L, mockCacheExampleInputView.filterListCount.get())
        /* Bind */
        bindCacheExample(mockCacheExampleInputView, CacheExampleState(ids = createStateHolder(listOf("", "", ""))))
        /* Assert view gets called with current state */
        assertEquals(1L, mockCacheExampleInputView.filterListCount.get())
    }

    @Test
    fun selectFiler_BindInput_InfoDisplayed() {
        val mockCacheExampleInputView: MockCacheExampleInputView = MockCacheExampleInputView()
        /* Pre-bind: assert view not called */
        assertEquals(0L, mockCacheExampleInputView.currentFilterCount.get())
        /* Bind */
        bindCacheExample(mockCacheExampleInputView, CacheExampleState(currentId = createStateHolder("")))
        /* Assert view gets called with current state */
        assertEquals(1L, mockCacheExampleInputView.currentFilterCount.get())
    }
}