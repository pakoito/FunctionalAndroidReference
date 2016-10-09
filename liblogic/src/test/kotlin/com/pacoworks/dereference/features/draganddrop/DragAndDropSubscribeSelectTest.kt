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

package com.pacoworks.dereference.features.draganddrop

import com.jakewharton.rxrelay.PublishRelay
import com.pacoworks.dereference.architecture.ui.createStateHolder
import org.javatuples.Pair
import org.junit.Test
import rx.observers.TestSubscriber

class DragAndDropSubscribeSelectTest {
    @Test
    fun selectEmpty_ClickElement_ElementSelected() {
        val listClicks = PublishRelay.create<Pair<Int, String>>()
        val selected = createStateHolder<Set<String>>(setOf())
        val testSubscriber = TestSubscriber<Set<String>>()
        selected.subscribe(testSubscriber)
        handleSelected(listClicks, selected)
        testSubscriber.assertValueCount(1)
        val selectedElement = "Paco"
        listClicks.call(Pair.with(0, selectedElement))
        testSubscriber.assertValueCount(2)
        testSubscriber.assertValues(setOf(), setOf(selectedElement))
        testSubscriber.assertNoTerminalEvent()
    }

    @Test
    fun selectElement_ClickElement_ElementUnselected() {
        val listClicks = PublishRelay.create<Pair<Int, String>>()
        val selectedElement = "Paco"
        val selected = createStateHolder(setOf(selectedElement))
        val testSubscriber = TestSubscriber<Set<String>>()
        selected.subscribe(testSubscriber)
        handleSelected(listClicks, selected)
        testSubscriber.assertValueCount(1)
        listClicks.call(Pair.with(0, selectedElement))
        testSubscriber.assertValueCount(2)
        testSubscriber.assertValues(setOf(selectedElement), setOf())
        testSubscriber.assertNoTerminalEvent()
    }
}