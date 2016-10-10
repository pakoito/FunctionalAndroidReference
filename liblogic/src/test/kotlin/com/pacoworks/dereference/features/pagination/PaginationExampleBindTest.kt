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

package com.pacoworks.dereference.features.pagination

import com.jakewharton.rxrelay.SerializedRelay
import com.pacoworks.dereference.architecture.ui.createStateHolder
import com.pacoworks.dereference.bindAsTest
import com.pacoworks.dereference.mockView
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.functions.Action2
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

class PaginationExampleBindTest {
    @Test
    fun paginationValues_newValues_DisplayValues() {
        val viewInput = MockPaginationExampleInputView()
        val startList = listOf<String>()
        val newList = listOf("1")
        val elements = createStateHolder(startList)
        val state = PaginationExampleState(elements = elements)
        assertEquals(0, viewInput.callCount.get())
        bindPaginationExample(viewInput, state)
        assertEquals(1, viewInput.callCount.get())
        assertEquals(startList, viewInput.value.get())
        elements.call(newList)
        assertEquals(2, viewInput.callCount.get())
        assertEquals(newList, viewInput.value.get())
    }
}

class MockPaginationExampleInputView : PaginationExampleInputView {
    val value = AtomicReference<List<String>>()

    val callCount = AtomicLong()

    override fun updateElements(elements: List<String>) =
            mockView(callCount, value).invoke(elements)

    override fun <T> createBinder(): Action2<SerializedRelay<T, T>, (T) -> Unit> =
            bindAsTest()

}
