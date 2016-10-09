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

import com.jakewharton.rxrelay.SerializedRelay
import com.pacoworks.dereference.architecture.ui.createStateHolder
import com.pacoworks.dereference.bindAsTest
import com.pacoworks.dereference.features.list.ListExampleState
import com.pacoworks.dereference.mockView
import org.junit.Assert
import org.junit.Test
import rx.functions.Action2
import java.util.concurrent.atomic.AtomicLong

class DragAndDropBindTest {
    @Test
    fun elements_Bind_InfoDisplayed() {
        val viewInput = MockDragAndDropViewInput()
        val state = ListExampleState(elements = createStateHolder(listOf("1", "2")), selected = createStateHolder(setOf("1")))
        Assert.assertEquals(0L, viewInput.updateElementsCount.get())
        bindDragAndDropExample(viewInput, state)
        Assert.assertEquals(1L, viewInput.updateElementsCount.get())
    }

    @Test
    fun select_Bind_InfoDisplayed() {
        val viewInput = MockDragAndDropViewInput()
        val state = ListExampleState(elements = createStateHolder(listOf("1", "2")), selected = createStateHolder(setOf("1")))
        Assert.assertEquals(0L, viewInput.updateSelectedCount.get())
        bindDragAndDropExample(viewInput, state)
        Assert.assertEquals(1L, viewInput.updateSelectedCount.get())
    }
}

class MockDragAndDropViewInput : DragAndDropInputView {
    val updateElementsCount = AtomicLong()

    val updateSelectedCount = AtomicLong()

    override fun updateElements(elements: List<String>) =
            mockView<List<String>>(updateElementsCount).invoke(elements)

    override fun updateSelected(selected: Set<String>) =
            mockView<Set<String>>(updateSelectedCount).invoke(selected)

    override fun <T> createBinder(): Action2<SerializedRelay<T, T>, (T) -> Unit> =
            bindAsTest()

}
