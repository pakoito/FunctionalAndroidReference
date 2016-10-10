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

import com.pacoworks.dereference.architecture.ui.createStateHolder
import com.pacoworks.dereference.features.list.model.createEditModeDelete
import com.pacoworks.dereference.features.list.model.createEditModeNormal
import org.junit.Assert.assertEquals
import org.junit.Test

class ListBindTest {
    @Test
    fun editModeView_NewState_StateDisplayed() {
        val editModeState = createStateHolder(createEditModeNormal())
        val state = ListExampleState(editMode = editModeState)
        val viewInput = MockListInputExampleView()
        /* Pre-bind */
        assertEquals(0, viewInput.editModeCount.get())
        bindListExample(viewInput, state)
        /* Initial state */
        assertEquals(1, viewInput.editModeCount.get())
        /* New states */
        editModeState.call(createEditModeDelete("dfd"))
        assertEquals(2, viewInput.editModeCount.get())
    }

    @Test
    fun elementsView_NewState_StateDisplayed() {
        val elements = createStateHolder<List<String>>(listOf())
        val state = ListExampleState(elements = elements)
        val viewInput = MockListInputExampleView()
        /* Pre-bind */
        assertEquals(0, viewInput.elementsCount.get())
        bindListExample(viewInput, state)
        /* Initial state */
        assertEquals(1, viewInput.elementsCount.get())
        /* New states */
        elements.call(listOf("dfd"))
        assertEquals(2, viewInput.elementsCount.get())
    }

    @Test
    fun selectedView_NewState_StateDisplayed() {
        val selected = createStateHolder<Set<String>>(setOf())
        val state = ListExampleState(selected = selected)
        val viewInput = MockListInputExampleView()
        /* Pre-bind */
        assertEquals(0, viewInput.selectedCount.get())
        bindListExample(viewInput, state)
        /* Initial state */
        assertEquals(1, viewInput.selectedCount.get())
        /* New states */
        selected.call(setOf("dfd"))
        assertEquals(2, viewInput.selectedCount.get())
    }
}