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

class DragAndDropSubscribeDragAndDropTest {
    @Test
    fun elementsInList_dragAndDrop_ElementsSwapped() {
        val initialList = listOf("0", "1")
        val elements = createStateHolder(initialList)
        val dragAndDrops = PublishRelay.create<Pair<Int, Int>>()
        handleDragAndDrop(dragAndDrops, elements)
        val testSubscriber = TestSubscriber<List<String>>()
        elements.subscribe(testSubscriber)
        testSubscriber.assertValueCount(1)
        dragAndDrops.call(Pair.with(0, 1))
        val swappedList = listOf("1", "0")
        testSubscriber.assertValueCount(2)
        testSubscriber.assertValues(initialList, swappedList)
        testSubscriber.assertNoTerminalEvent()
    }
}