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

package com.pacoworks.dereference.features.list

import com.jakewharton.rxrelay.PublishRelay
import com.pacoworks.dereference.architecture.ui.createStateHolder
import com.pacoworks.dereference.core.functional.None
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.observers.TestSubscriber

class ListSubscribeAddTest {
    @Test
    fun emptyList_addElement_ElementInList() {
        val initialState = listOf<String>()
        val elementsState = createStateHolder(initialState)
        val addClick = PublishRelay.create<None>()
        val testSubscriber = TestSubscriber.create<List<String>>()
        handleAdd(elementsState, addClick)
        elementsState.subscribe(testSubscriber)
        /* Initial state */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(initialState)
        /* Click to add element */
        addClick.call(None)
        /* See new element added */
        testSubscriber.assertValueCount(2)
        /* Assert that 1 element was added to the list. Element is random, so we only check for existence. */
        val onNextEvents = testSubscriber.onNextEvents
        assertEquals(initialState.size + 1, onNextEvents[1].size)
        testSubscriber.assertNoTerminalEvent()
    }

    @Test
    fun filledList_addElement_ElementAppendedAtEnd() {
        val initialState = listOf("1", "2", "3")
        val elementsState = createStateHolder(initialState)
        val addClick = PublishRelay.create<None>()
        val testSubscriber = TestSubscriber.create<List<String>>()
        handleAdd(elementsState, addClick)
        elementsState.subscribe(testSubscriber)
        /* Initial state */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(initialState)
        /* Click to add element */
        addClick.call(None)
        /* See new element added */
        testSubscriber.assertValueCount(2)
        /* Assert that 1 element was added to the list. Element is random, so we only check for existence. */
        val onNextEvents = testSubscriber.onNextEvents
        val newList = onNextEvents[1]
        assertEquals(initialState.size + 1, newList.size)
        /* Check that previous elements are on the list on the same order */
        assertEquals(initialState[0], newList[0])
        assertEquals(initialState[1], newList[1])
        assertEquals(initialState[2], newList[2])
        testSubscriber.assertNoTerminalEvent()
    }

}