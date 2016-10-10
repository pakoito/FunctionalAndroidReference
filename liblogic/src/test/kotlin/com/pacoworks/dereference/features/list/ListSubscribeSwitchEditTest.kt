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
import org.junit.Test
import rx.observers.TestSubscriber

class ListSubscribeSwitchEditTest {
    @Test
    fun normalMode_SelectedToEdit_ElementSelected() {
        val editModeInitial = createEditModeNormal()
        val editMode = createStateHolder(editModeInitial)
        val selectedInitial = setOf<String>()
        val selected = createStateHolder(selectedInitial)
        val testSubscriber = TestSubscriber.create<Set<String>>()
        handleOnSwitchEditState(editMode, selected)
        selected.subscribe(testSubscriber)
        /* Initial state */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(selectedInitial)
        /* Change selected mode */
        val editId = "32"
        editMode.call(createEditModeDelete(editId))
        /* New selected value */
        testSubscriber.assertValueCount(2)
        val selectedExpected = selectedInitial.plus(editId)
        testSubscriber.assertValues(selectedInitial, selectedExpected)
        testSubscriber.assertNoTerminalEvent()
    }

    @Test
    fun editMode_DeleteElements_SelectedEmptied() {
        val editId = "12"
        val editModeInitial = createEditModeDelete(editId)
        val editMode = createStateHolder(editModeInitial)
        val selectedInitial = setOf(editId)
        val selected = createStateHolder(selectedInitial)
        val testSubscriber = TestSubscriber.create<Set<String>>()
        handleOnSwitchEditState(editMode, selected)
        selected.subscribe(testSubscriber)
        /* Initial state */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(selectedInitial)
        /* Change selected mode */
        editMode.call(createEditModeNormal())
        /* Selected list emptied */
        testSubscriber.assertValueCount(2)
        val selectedExpected = setOf<String>()
        testSubscriber.assertValues(selectedInitial, selectedExpected)
        testSubscriber.assertNoTerminalEvent()
    }

}