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
import com.pacoworks.dereference.features.list.model.createEditModeDelete
import com.pacoworks.dereference.features.list.model.createEditModeNormal
import org.javatuples.Pair
import org.junit.Test
import rx.observers.TestSubscriber

class ListSubscribeSelectTest {
    @Test
    fun normalMode_TapElement_NoSelect() {
        val listClicks = PublishRelay.create<Pair<Int, String>>()
        val editMode = createStateHolder(createEditModeNormal())
        val initialSelected = setOf<String>()
        val selected = createStateHolder(initialSelected)
        val testSubscriber = TestSubscriber.create<Set<String>>()
        handleSelect(editMode, selected, listClicks)
        selected.subscribe(testSubscriber)
        /* Initial state */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(initialSelected)
        /* Selected taps */
        listClicks.call(Pair.with(0, "15"))
        listClicks.call(Pair.with(0, "15"))
        listClicks.call(Pair.with(0, "14"))
        listClicks.call(Pair.with(0, "13"))
        listClicks.call(Pair.with(0, "15"))
        /* Assert no new elements selected */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(initialSelected)
    }

    @Test
    fun editMode_TapUnselectedElement_AddElementToSelected() {
        val listClicks = PublishRelay.create<Pair<Int, String>>()
        val editMode = createStateHolder(createEditModeDelete("75"))
        val initialSelected = setOf<String>()
        val selected = createStateHolder(initialSelected)
        val testSubscriber = TestSubscriber.create<Set<String>>()
        handleSelect(editMode, selected, listClicks)
        selected.subscribe(testSubscriber)
        /* Initial state */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(initialSelected)
        /* Tap on element */
        val tapped = "15"
        listClicks.call(Pair.with(0, tapped))
        /* Assert element has been added to selected */
        testSubscriber.assertValueCount(2)
        val expectedSelected = initialSelected.plus(tapped)
        testSubscriber.assertValues(initialSelected, expectedSelected)
    }

    @Test
    fun editMode_TapSelectedElement_RemoveElementFromSelected() {
        val element = "75"
        val listClicks = PublishRelay.create<Pair<Int, String>>()
        val editMode = createStateHolder(createEditModeDelete("85"))
        val initialSelected = setOf("12", element)
        val selected = createStateHolder(initialSelected)
        val testSubscriber = TestSubscriber.create<Set<String>>()
        handleSelect(editMode, selected, listClicks)
        selected.subscribe(testSubscriber)
        /* Initial state */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(initialSelected)
        /* Selected taps */
        listClicks.call(Pair.with(0, element))
        /* Assert element has been removed from selected */
        testSubscriber.assertValueCount(2)
        val expectedSelected = initialSelected.minus(element)
        testSubscriber.assertValues(initialSelected, expectedSelected)
    }

}